package dev.hawu.plugins.api;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import dev.hawu.plugins.api.reflect.UncheckedReflects;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.PluginCommand;
import org.bukkit.command.SimpleCommandMap;
import org.bukkit.event.Event;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.RegisteredListener;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.net.*;
import java.nio.channels.Channels;
import java.nio.file.Files;
import java.util.*;
import java.util.stream.Stream;

@SuppressWarnings({"deprecation", "DuplicatedCode"})
public final class PackagesManager {

    private static final long FIVE_MINUTES = 5 * 60 * 1000;
    private static final String INFO_PREFIX = "&a&l(!)&a Info &8| &a";
    private static final String WARN_PREFIX = "&6&l(!)&6 Warn &8| &6";
    private static final String SEVERE_PREFIX = "&c&l(!)&c Severe &8| &6";
    private static final JsonParser parser = new JsonParser();
    private static final Method GET_FILE = UncheckedReflects.getMethod(JavaPlugin.class, true, "getFile");
    private static PackagesManager instance;
    private final JavaPlugin plugin;
    private final Map<String, PluginPackage> packagesMap = new HashMap<>();
    private final String PACKAGES_LINK = "https://plugins.hawu.dev/api/v1/projects";
    private long lastCache = -1;
    private volatile boolean syncing = false;

    private PackagesManager(final @NotNull JavaPlugin plugin) {
        this.plugin = plugin;
    }

    @NotNull
    public static PackagesManager getInstance() {
        if(instance == null) return instance = new PackagesManager(HikariLibrary.getInstance());
        return instance;
    }

    public static void infoIfNotNull(final @Nullable CommandSender sender, final @NotNull String message) {
        if(sender != null) sender.sendMessage(Strings.color(INFO_PREFIX + message));
    }

    public static void warnIfNotNull(final @Nullable CommandSender sender, final @NotNull String message) {
        if(sender != null) sender.sendMessage(Strings.color(WARN_PREFIX + message));
    }

    public static void severeIfNotNull(final @Nullable CommandSender sender, final @NotNull String message) {
        if(sender != null) sender.sendMessage(Strings.color(SEVERE_PREFIX + message));
    }

    private String computeReleasesLink(final @NotNull String id, final int page) {
        final StringBuilder sb = new StringBuilder();
        sb.append(PACKAGES_LINK).append("/").append(id).append("?page=").append(page);
        if(HikariLibrary.getAuthToken() != null) sb.append("&token=").append(HikariLibrary.getAuthToken());
        return sb.toString();
    }

    private String computeDownloadLink(final @NotNull String id, final @NotNull String tag) {
        final StringBuilder sb = new StringBuilder();
        sb.append(PACKAGES_LINK).append("/").append(id).append("/").append(tag);
        if(HikariLibrary.getAuthToken() != null) sb.append("?token=").append(HikariLibrary.getAuthToken()).append("&tag=download");
        else sb.append("?tag=download");
        return sb.toString();
    }

    @NotNull
    private List<@NotNull Release> cacheReleases(final @NotNull String id, final int page) throws Exception {
        final HttpURLConnection connection = (HttpURLConnection) new URL(computeReleasesLink(id, page)).openConnection();
        final List<Release> releases = new ArrayList<>();
        if(connection.getResponseCode() == 404) {
            return releases;
        }

        final JsonArray array = parser.parse(new InputStreamReader(connection.getInputStream())).getAsJsonArray();
        for(final JsonElement element : array) {
            final JsonObject obj = element.getAsJsonObject();
            final List<Asset> assets = new ArrayList<>();

            for(final JsonElement e : obj.get("assets").getAsJsonArray()) {
                final JsonObject asset = e.getAsJsonObject();

                final long idNum = asset.get("id").getAsLong();
                final String name = asset.get("name").getAsString();
                final String label = asset.get("label").isJsonNull() ? null : asset.get("label").getAsString();
                final String contentType = asset.get("content_type").getAsString();
                final long size = asset.get("size").getAsLong();
                final String downloadURL = asset.get("browser_download_url").getAsString();

                assets.add(new Asset(idNum, name, label, contentType, size, downloadURL));
            }

            releases.add(new Release(obj.get("id").getAsLong(), obj.get("name").getAsString(), obj.get("tag_name").getAsString(),
                obj.get("prerelease").getAsBoolean(), assets));
        }

        releases.addAll(cacheReleases(id, page + 1));
        return releases;
    }

    public void cache(final @Nullable CommandSender sender, final @NotNull Runnable action, final boolean async) {
        if(syncing || (lastCache > 0 && System.currentTimeMillis() - lastCache <= FIVE_MINUTES)) {
            if(async) Tasks.scheduleAsync(plugin, runnable -> action.run());
            else action.run();
            return;
        }

        syncing = true;
        infoIfNotNull(sender, "Attempting to synchronise the cache with the remote database");
        lastCache = System.currentTimeMillis();

        Tasks.scheduleAsync(plugin, runnable -> {
            try {
                packagesMap.clear();

                final URLConnection connection = new URL(PACKAGES_LINK).openConnection();
                connection.setConnectTimeout(30 * 1000);

                final JsonArray array = parser.parse(new InputStreamReader(connection.getInputStream())).getAsJsonArray();
                for(final JsonElement element : array) {
                    final JsonObject obj = element.getAsJsonObject();

                    final String id = obj.get("id").getAsString();
                    final List<String> dependencies = new ArrayList<>();

                    for(final JsonElement dependency : obj.get("dependencies").getAsJsonArray()) {
                        dependencies.add(dependency.getAsString());
                    }

                    packagesMap.put(id, new PluginPackage(id, obj.get("name").getAsString(), obj.get("description").getAsString(),
                        obj.get("compatibility").getAsString(), obj.get("premium").getAsBoolean(),
                        dependencies, cacheReleases(id, 1)));
                }

                infoIfNotNull(sender, "Synchronisation completed with no errors.");
                if(async) action.run();
                else Tasks.schedule(plugin, bukkitRunnable -> action.run());

                syncing = false;
            } catch(final SocketTimeoutException timeoutException) {
                severeIfNotNull(sender, "Connection timed out after 30 seconds.");
            } catch(final Exception exception) {
                severeIfNotNull(sender, "Unexpected error occurred while caching packages.");
                exception.printStackTrace();
            }
        });
    }

    @NotNull
    public Stream<@NotNull PluginPackage> filterPackages(final @NotNull String query, final boolean exactMatch,
                                                         final boolean byId, final boolean byName, final int count) {
        final String lowercasedQuery = query.toLowerCase();
        return getPackages().stream()
            .filter(pkg -> {
                final String lowercasedId = pkg.getId().toLowerCase();
                final String lowercasedName = pkg.getName().toLowerCase();

                if(byId) return exactMatch ? pkg.getId().equalsIgnoreCase(query) : lowercasedId.contains(lowercasedQuery);
                if(byName) return exactMatch ? pkg.getName().equalsIgnoreCase(query) : lowercasedName.contains(lowercasedQuery);
                return exactMatch ? pkg.getId().equalsIgnoreCase(query) || pkg.getName().equalsIgnoreCase(query)
                    : lowercasedId.contains(lowercasedQuery) || lowercasedName.contains(lowercasedQuery);
            })
            .limit(count > 0 ? count : getPackages().size());
    }

    public void installPackage(final @Nullable CommandSender sender, final @NotNull PluginPackage pkg, final @NotNull String version,
                               final boolean verbose, final boolean load, final boolean force) {
        if(isPackageInstalled(pkg.getId())) {
            severeIfNotNull(sender, "This package is already installed. You are probably looking to &eupgrade&c or &euninstall&c.");
            return;
        }

        if(Bukkit.getPluginManager().getPlugin(pkg.getName()) != null && !force) {
            severeIfNotNull(sender, "There's already an unrecognized plugin with the same name installed.");
            return;
        }

        if(!pkg.hasRelease(version)) {
            severeIfNotNull(sender, String.format("No release found matching %s for %s.", version, pkg.getName()));
            return;
        }

        try {
            if(verbose) infoIfNotNull(sender, "Establishing connection for downloading");
            final HttpURLConnection connection = (HttpURLConnection) new URL(computeDownloadLink(pkg.getId(), version)).openConnection();
            connection.setConnectTimeout(30 * 60 * 1000);

            if(connection.getResponseCode() == 404) {
                warnIfNotNull(sender, "Unauthorized access or resource not found.");
                return;
            }

            if(verbose) infoIfNotNull(sender, "Resolving the plugins folder for installation");
            final File pluginsFolder = plugin.getDataFolder().getParentFile();
            final File file = new File(pluginsFolder, pkg.getName() + ".jar");

            if(file.exists()) {
                severeIfNotNull(sender, String.format("There's already a file in the plugins folder with named %s.jar", pkg.getName()));
                return;
            }

            if(!file.createNewFile()) {
                severeIfNotNull(sender, "Could not create a new file for transfer.");
                return;
            }
            if(verbose) infoIfNotNull(sender, "Starting the downloading process");

            final long time = System.currentTimeMillis();
            new FileOutputStream(file).getChannel().transferFrom(Channels.newChannel(connection.getInputStream()), 0, Long.MAX_VALUE);
            final long duration = System.currentTimeMillis() - time;

            final double length = file.length() / 1000.0;
            final double seconds = duration / 1000.0;
            infoIfNotNull(sender, String.format("Transferred &e%skB &ain &e%ss&8 (%s).", Strings.format(length),
                Strings.format(seconds), Strings.format(length / seconds) + "kB/s"));

            pkg.getDependencies().forEach(dependency -> {
                if(Bukkit.getPluginManager().getPlugin(dependency) == null) {
                    warnIfNotNull(sender, String.format("Plugin %s specified an unknown dependency: %s", pkg.getName(), dependency));
                }
            });

            if(load) {
                infoIfNotNull(sender, String.format("Attempting to load plugin %s", pkg.getName()));
                Bukkit.getPluginManager().enablePlugin(Bukkit.getPluginManager().loadPlugin(file));
            }
        } catch(final SocketTimeoutException exception) {
            severeIfNotNull(sender, "Connection timed out.");
        } catch(final Exception exception) {
            severeIfNotNull(sender, "Error occurred while installing package.");
            exception.printStackTrace();
        }
    }

    private void deleteRecursively(final @Nullable File file) {
        if(file == null) return;

        if(file.isFile()) {
            try {
                Files.deleteIfExists(file.toPath());
            } catch(final IOException ignored) {}
            return;
        }

        final File[] list = file.listFiles();
        if(list != null && list.length > 0) {
            for(final File f : list) {
                deleteRecursively(f);
            }
        } else {
            try {
                Files.deleteIfExists(file.toPath());
            } catch(final IOException ignored) {}
        }
    }

    /**
     * Unloads a specific plugin package before removing.
     * <p>
     * This piece of code is licensed by PlugMan by r-clancy
     * on GitHub.
     *
     * @param sender  The sender for message sending.
     * @param plugin  The plugin to unload.
     * @param verbose Whether to log everything.
     * @param noGC    Whether to run the garbage collector.
     * @param delete  Whether to delete files.
     */
    @SuppressWarnings({"unchecked", "ConstantConditions"})
    private void unloadPlugin(final @Nullable CommandSender sender, final @NotNull Plugin plugin,
                              final boolean verbose, final boolean noGC, final boolean delete) {
        if(verbose) infoIfNotNull(sender, String.format("Preparing to unload %s", plugin.getName()));
        final String name = plugin.getName();
        final File dataFolder = plugin.getDataFolder();
        final File pluginFile = getPluginFile(plugin);
        PluginManager pluginManager = Bukkit.getPluginManager();
        SimpleCommandMap commandMap = null;
        List<Plugin> plugins = null;
        Map<String, Plugin> names = null;
        Map<String, Command> commands = null;
        Map<Event, SortedSet<RegisteredListener>> listeners = null;

        boolean reloadlisteners = true;

        if(pluginManager != null) {
            pluginManager.disablePlugin(plugin);
            try {
                if(verbose) infoIfNotNull(sender, "Retrieving related fields");
                Field pluginsField = Bukkit.getPluginManager().getClass().getDeclaredField("plugins");
                pluginsField.setAccessible(true);
                plugins = (List<Plugin>) pluginsField.get(pluginManager);

                Field lookupNamesField = Bukkit.getPluginManager().getClass().getDeclaredField("lookupNames");
                lookupNamesField.setAccessible(true);
                names = (Map<String, Plugin>) lookupNamesField.get(pluginManager);

                try {
                    if(verbose) infoIfNotNull(sender, "Retrieving plugin's listeners");
                    Field listenersField = Bukkit.getPluginManager().getClass().getDeclaredField("listeners");
                    listenersField.setAccessible(true);
                    listeners = (Map<Event, SortedSet<RegisteredListener>>) listenersField.get(pluginManager);
                } catch(final Exception e) {
                    reloadlisteners = false;
                }

                if(verbose) infoIfNotNull(sender, "Retrieving plugin's commands");
                Field commandMapField = Bukkit.getPluginManager().getClass().getDeclaredField("commandMap");
                commandMapField.setAccessible(true);
                commandMap = (SimpleCommandMap) commandMapField.get(pluginManager);

                Field knownCommandsField = SimpleCommandMap.class.getDeclaredField("knownCommands");
                knownCommandsField.setAccessible(true);
                commands = (Map<String, Command>) knownCommandsField.get(commandMap);
            } catch(final NoSuchFieldException | IllegalAccessException e) {
                e.printStackTrace();
                severeIfNotNull(sender, String.format("Failed to unload plugin %s", name));
                return;
            }
        }

        if(verbose) infoIfNotNull(sender, "Unregistering the plugin from Bukkit");
        if(plugins != null) plugins.remove(plugin);
        if(names != null) names.remove(name);

        if(verbose) infoIfNotNull(sender, "Unregistering the plugin's associated listeners");
        if(listeners != null && reloadlisteners) listeners.values().forEach(set -> set.removeIf(value -> value.getPlugin() == plugin));

        if(verbose) infoIfNotNull(sender, "Unregistering the plugin's commands");
        if(commandMap != null) {
            for(Iterator<Map.Entry<String, Command>> it = commands.entrySet().iterator(); it.hasNext(); ) {
                Map.Entry<String, Command> entry = it.next();
                if(entry.getValue() instanceof PluginCommand) {
                    PluginCommand c = (PluginCommand) entry.getValue();
                    if(c.getPlugin() == plugin) {
                        c.unregister(commandMap);
                        it.remove();
                    }
                }
            }
        }

        // Attempt to close the classloader to unlock any handles on the plugin's jar file.
        if(verbose) infoIfNotNull(sender, "Retrieving the plugin's class loader");
        ClassLoader cl = plugin.getClass().getClassLoader();
        if(cl instanceof URLClassLoader) {
            try {
                if(verbose) infoIfNotNull(sender, "Removing the plugin from the class loader");
                Field pluginField = cl.getClass().getDeclaredField("plugin");
                pluginField.setAccessible(true);
                pluginField.set(cl, null);
                Field pluginInitField = cl.getClass().getDeclaredField("pluginInit");
                pluginInitField.setAccessible(true);
                pluginInitField.set(cl, null);
            } catch(final NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException ex) {
                ex.printStackTrace();
                severeIfNotNull(sender, String.format("Couldn't unregister plugin %s", name));
            }

            try {
                if(verbose) infoIfNotNull(sender, "Closing the plugin's class loader");
                ((URLClassLoader) cl).close();
            } catch(IOException ex) {
                ex.printStackTrace();
                severeIfNotNull(sender, String.format("Couldn't close %s's class loader", name));
            }
        }

        // Will not work on processes started with the -XX:+DisableExplicitGC flag, but lets try it anyway.
        // This tries to get around the issue where Windows refuses to unlock jar files that were previously loaded into the JVM.
        if(!noGC) {
            if(verbose) infoIfNotNull(sender, "Attempting to suggest the garbage collector to run.");
            System.gc();
        }

        // Delete files if necessary
        if(delete) {
            if(verbose) infoIfNotNull(sender, "Deleting the plugin's files");
            deleteRecursively(dataFolder);
        }

        deleteRecursively(pluginFile);
    }

    public void uninstallPackage(final @Nullable CommandSender sender, final @NotNull PluginPackage pkg, final boolean verbose,
                                 final boolean noGC, final boolean delete, final boolean force) {
        if(!PackagesManager.getInstance().isPackageInstalled(pkg.getId())
            && (Bukkit.getPluginManager().getPlugin(pkg.getName()) == null || force)) {
            PackagesManager.severeIfNotNull(sender, "That package is not installed on this server.");
            return;
        }

        unloadPlugin(sender, Bukkit.getPluginManager().getPlugin(pkg.getName()), verbose, noGC, delete);
    }

    private File getPluginFile(final @NotNull Plugin plugin) {
        try {
            if(!GET_FILE.isAccessible()) GET_FILE.setAccessible(true);
            return (File) GET_FILE.invoke(plugin);
        } catch(final Throwable exception) {
            exception.printStackTrace();
            return null;
        }
    }

    public void upgradePackage(final @Nullable CommandSender sender, final @NotNull PluginPackage pkg, final @NotNull String version,
                               final boolean verbose, final boolean force) {
        if(!PackagesManager.getInstance().isPackageInstalled(pkg.getId())
            && (Bukkit.getPluginManager().getPlugin(pkg.getName()) == null || force)) {
            PackagesManager.severeIfNotNull(sender, "That package is not installed on this server.");
            return;
        }

        if(Bukkit.getPluginManager().getPlugin(pkg.getName()).getDescription().getVersion().equals(version)) {
            severeIfNotNull(sender, "That package is already up to date.");
            return;
        }

        if(verbose) infoIfNotNull(sender, "Preparing to upgrade");
        final Plugin plugin = Bukkit.getPluginManager().getPlugin(pkg.getName());
        final File pluginFile = getPluginFile(plugin);

        if(pluginFile == null) {
            severeIfNotNull(sender, "There's a problem finding the plugin's file.");
            return;
        }

        if(!pkg.hasRelease(version)) {
            severeIfNotNull(sender, String.format("No release found matching %s for %s.", version, pkg.getName()));
            return;
        }

        try {
            if(verbose) infoIfNotNull(sender, "Establishing connection for downloading");
            final HttpURLConnection connection = (HttpURLConnection) new URL(computeDownloadLink(pkg.getId(), version)).openConnection();
            connection.setConnectTimeout(30 * 60 * 1000);

            if(connection.getResponseCode() == 404) {
                warnIfNotNull(sender, "Unauthorized access or resource not found.");
                return;
            }

            if(verbose) infoIfNotNull(sender, "Resolving the update folder for installation");
            final File file = new File(Bukkit.getUpdateFolderFile(), pluginFile.getName());

            if(file.exists()) {
                severeIfNotNull(sender, String.format("There's already a file in the update folder with named %s.jar", pkg.getName()));
                return;
            }

            if(!file.createNewFile()) {
                severeIfNotNull(sender, "Could not create a new file for transfer.");
                return;
            }
            if(verbose) infoIfNotNull(sender, "Starting the downloading process");

            final long time = System.currentTimeMillis();
            new FileOutputStream(file).getChannel().transferFrom(Channels.newChannel(connection.getInputStream()), 0, Long.MAX_VALUE);
            final long duration = System.currentTimeMillis() - time;

            final double length = file.length() / 1000.0;
            final double seconds = duration / 1000.0;
            infoIfNotNull(sender, String.format("Transferred &e%skB &ain &e%ss&8 (%s).", Strings.format(length),
                Strings.format(seconds), Strings.format(length / seconds) + "kB/s"));

            pkg.getDependencies().forEach(dependency -> {
                if(Bukkit.getPluginManager().getPlugin(dependency) == null)
                    warnIfNotNull(sender, String.format("Plugin %s specified an unknown dependency: %s", pkg.getName(), dependency));
            });

            infoIfNotNull(sender, "You may now restart or reload the server to install the update.");
        } catch(final SocketTimeoutException exception) {
            severeIfNotNull(sender, "Connection timed out.");
        } catch(final Exception exception) {
            severeIfNotNull(sender, "Error occurred while upgrading package.");
            exception.printStackTrace();
        }
    }

    @NotNull
    public Collection<@NotNull PluginPackage> getPackages() {
        return packagesMap.values();
    }

    public boolean isPackageInstalled(final @NotNull String id) {
        final PluginPackage pkg = packagesMap.get(id.toLowerCase());
        final Plugin plugin = Bukkit.getPluginManager().getPlugin(pkg.getName());
        return plugin != null && plugin.getDescription().getMain().startsWith("dev.hawu");
    }

}
