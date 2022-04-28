package dev.hawu.plugins.api.i18n;

import dev.hawu.plugins.api.ObjectUtils;
import dev.hawu.plugins.api.Strings;
import dev.hawu.plugins.api.collections.tuples.Pair;
import dev.hawu.plugins.api.items.ItemStackBuilder;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.InputStream;
import java.text.MessageFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * The module representing a language.
 *
 * @since 1.6
 */
public final class ResourceModule {

    private final JavaPlugin plugin;
    private final String moduleName;
    private final Locale defaultLocale;
    private final Map<Locale, FileConfiguration> configurationMap;

    /**
     * Constructs a new resource module with English as default.
     *
     * @param moduleName the name of the module
     * @since 1.6
     */
    public ResourceModule(final @NotNull String moduleName, final @NotNull JavaPlugin plugin) {
        this(moduleName, plugin, Locale.en_US);
    }

    /**
     * Constructs a new resource module with a custom default locale.
     *
     * @param moduleName    the name of the module
     * @param defaultLocale the default locale
     * @since 1.6
     */
    public ResourceModule(final @NotNull String moduleName, final @NotNull JavaPlugin plugin, final @NotNull Locale defaultLocale) {
        this.moduleName = moduleName;
        this.plugin = plugin;
        this.defaultLocale = defaultLocale;
        this.configurationMap = new HashMap<>();
        this.reloadConfigurations();
    }

    /**
     * Reloads dedicated configurations.
     *
     * @since 1.6
     */
    @SuppressWarnings("ResultOfMethodCallIgnored")
    public void reloadConfigurations() {
        final String path = MessageFormat.format("lang/{0}", moduleName);
        final File folder = new File(plugin.getDataFolder(), path);
        if(!folder.exists()) folder.mkdirs();

        synchronized(configurationMap) {
            configurationMap.clear();
            for(final Locale locale : Locale.values()) {
                final File file = new File(folder, MessageFormat.format("{0}_{1}.yml", moduleName, locale.name()));
                if(!file.exists()) {
                    final InputStream stream = plugin.getResource(MessageFormat.format("{0}/{1}_{2}.yml", path, moduleName, locale.name()));
                    if(stream != null) {
                        plugin.saveResource(MessageFormat.format("{0}/{1}_{2}.yml", path, moduleName, locale.name()), true);
                        plugin.getLogger().info(MessageFormat.format("Created new language file for {0} locale {1}", moduleName, locale.name()));
                    } else continue; // Ignore the locale if not found. Let the user do it.
                }

                final YamlConfiguration configuration = YamlConfiguration.loadConfiguration(file);
                configurationMap.put(locale, configuration);
            }
        }
    }

    private String extractString(final String key, final FileConfiguration config) {
        if(config.isList(key)) {
            final StringBuilder builder = new StringBuilder();
            config.getStringList(key).forEach(builder::append);
            return Strings.color(builder.toString());
        }

        return Strings.color(config.getString(key));
    }

    /**
     * Translates the string in a locale then replaces the placeholders.
     *
     * @param locale the locale
     * @param key    the key
     * @param args   the arguments
     * @return the translated string
     * @since 1.6
     */
    @NotNull
    public String translate(final @NotNull Locale locale, final @NotNull String key, final @NotNull Pair<?, ?>... args) {
        final FileConfiguration configuration = ObjectUtils.elvis(configurationMap.get(locale), configurationMap.get(defaultLocale));
        if(configuration == null) return key;
        return Strings.fillPlaceholders(extractString(key, configuration), args);
    }

    /**
     * Translates the string in the default locale then replaces the placeholders.
     *
     * @param key  the key
     * @param args the arguments
     * @return the translated string
     * @since 1.6
     */
    @NotNull
    public String translate(final @NotNull String key, final @NotNull Pair<?, ?>... args) {
        return translate(defaultLocale, key, args);
    }

    /**
     * Translates the provided item stack in a locale, replaces placeholders
     * and returns a newly created item.
     *
     * @param locale the locale
     * @param item   the item
     * @param key    the key
     * @param args   the arguments
     * @return the translated item
     * @since 1.6
     */
    @NotNull
    public ItemStack translateItem(final @NotNull Locale locale, final @NotNull ItemStack item, final @NotNull String key, final @NotNull Pair<?, ?>... args) {
        final FileConfiguration configuration = ObjectUtils.elvis(configurationMap.get(locale), configurationMap.get(defaultLocale));
        final String displayName = Strings.color(configuration.getString(key + ".name", ""));
        final List<String> lore = configuration.getStringList(key + ".lore").stream().map(Strings::color).collect(Collectors.toList());
        return ItemStackBuilder.from(item)
            .name(displayName)
            .lore(lore)
            .replaceText(args)
            .build();
    }

    /**
     * Translates the provided item stack in the default locale, replaces placeholders
     * and returns a newly created item.
     *
     * @param item the item
     * @param key  the key
     * @param args the arguments
     * @return the translated item
     * @since 1.6
     */
    @NotNull
    public ItemStack translateItem(final @NotNull ItemStack item, final @NotNull String key, final @NotNull Pair<?, ?>... args) {
        return translateItem(defaultLocale, item, key, args);
    }

}
