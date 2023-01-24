package dev.hawu.plugins.api.i18n;

import dev.hawu.plugins.api.ObjectUtils;
import dev.hawu.plugins.api.Strings;
import dev.hawu.plugins.api.collections.tuples.Pair;
import dev.hawu.plugins.api.items.ItemStackBuilder;
import dev.hawu.plugins.api.misc.ConsoleLogger;
import org.bukkit.ChatColor;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * Represents a wrapper for a messages file
 * with .properties syntax.
 *
 * @since 2.0
 */
public final class LanguageModule {

    private final JavaPlugin plugin;
    private final String moduleName;
    private final Locale defaultLocale;
    private final Map<Locale, Map<String, String>> messages;

    /**
     * Constructs a new language model with default locale as English.
     *
     * @param plugin The plugin instance.
     * @param name   The name of the module.
     * @since 2.0
     */
    public LanguageModule(final JavaPlugin plugin, final String name) {
        this(plugin, name, Locale.en_US);
    }

    /**
     * Constructs a language model with a custom default locale.
     *
     * @param plugin        The plugin instance.
     * @param name          The name of the module.
     * @param defaultLocale The default locale.
     * @since 2.0
     */
    public LanguageModule(final @NotNull JavaPlugin plugin, final @NotNull String name, final @NotNull Locale defaultLocale) {
        this.plugin = plugin;
        this.moduleName = name;
        this.defaultLocale = defaultLocale;
        this.messages = new HashMap<>();

        this.saveResources(false);
    }

    /**
     * Translates the key into a message, with the default locale.
     *
     * @param key  The key to translate.
     * @param args The arguments to format the message with.
     * @return The translated message.
     * @since 2.0
     */
    public String translate(final @NotNull String key, final @NotNull Pair<?, ?>... args) {
        return translate(defaultLocale, key, args);
    }

    /**
     * Translates the key into a message, with a custom locale.
     *
     * @param locale The locale to translate with.
     * @param key    The key to translate.
     * @param args   The arguments to format the message with.
     * @return The translated message.
     * @since 2.0
     */
    public String translate(final @NotNull Locale locale, final @NotNull String key, final @NotNull Pair<?, ?>... args) {
        final Map<String, String> map = ObjectUtils.elvis(messages.get(locale), messages.get(defaultLocale));
        if(map == null) return key;

        final String message = map.get(key);
        if(message == null)
            return locale == defaultLocale ? key : translate(defaultLocale, key, args);
        return Strings.fillPlaceholders(message, args);
    }

    /**
     * Translates variable messages on the item according to the default locale.
     *
     * @param item The item to translate.
     * @param key  The key to translate.
     * @param args The arguments to format the message with.
     * @return The translated item.
     * @since 2.0
     */
    @NotNull
    public ItemStack translateItem(final @NotNull ItemStack item, final @NotNull String key, final @NotNull Pair<?, ?>... args) {
        return translateItem(defaultLocale, item, key, args);
    }

    /**
     * Translates variable messages on the item according to the locale.
     * <p>
     * Requires the key for the display name to be in format {@code key.name}
     * and the lore to be in format {@code key.lore}.
     *
     * @param locale The locale to translate with.
     * @param item   The item to translate.
     * @param key    The key to translate.
     * @param args   The arguments to format the message with.
     * @return The translated item.
     * @since 2.0
     */
    @NotNull
    public ItemStack translateItem(final @NotNull Locale locale, final @NotNull ItemStack item, final @NotNull String key, final @NotNull Pair<?, ?>... args) {
        final Map<String, String> map = ObjectUtils.elvis(messages.get(locale), messages.get(defaultLocale));
        if(map == null) return item;

        final String displayName = map.getOrDefault(key + ".name", key + ".name");
        final List<String> rawLore = Arrays.stream(map.getOrDefault(key + ".lore", key + ".lore")
            .split("\n")).collect(Collectors.toList());

        // Replace lore in case of special spread placeholder.
        final List<String> lore = new ArrayList<>();
        for(final String rawLine : rawLore) {
            String line = rawLine;

            // Manually replacing args since the fillPlaceholders option does not support spread placeholders.
            // This is pretty much the same as fillPlaceholders except we're dealing with the spread placeholders ourselves.
            for(final Pair<?, ?> param : args) {
                final String target = String.valueOf(param.getFirst());

                // If second option is null, it doesn't matter.
                if(!param.getSecondOption().isPresent()) {
                    line = line.replace("%" + target + "%", "null");
                    continue;
                }

                // If the second option is NOT a COLLECTION, this is a normal placeholder, do normally.
                if(!(param.getSecond() instanceof Collection<?>)) {
                    line = line.replace("%" + target + "%", String.valueOf(param.getSecond()));
                    continue;
                }

                // This is a SPREAD placeholder. This can be spread out multiple lines.
                final Collection<?> collection = (Collection<?>) param.getSecond();
                final String[] sides = line.split("%" + target + "%");
                final StringBuilder currentLine = new StringBuilder();
                for(int i = 0; i < sides.length; i++) {
                    currentLine.append(sides[i]);
                    if(i != sides.length - 1) {
                        final String color = ChatColor.getLastColors(Strings.color(sides[i]));
                        final StringJoiner joiner = new StringJoiner("\n");
                        collection.stream().map(String::valueOf).map(s -> color + s).forEach(joiner::add);
                        currentLine.append(joiner);
                    }
                }

                line = currentLine.toString();
            }

            Arrays.stream(line.split("\\n"))
                .map(String::trim)
                .map(Strings::color)
                .forEach(lore::add);
        }

        return ItemStackBuilder.from(item)
            .name(displayName)
            .lore(lore)
            .replaceText(args) // Just to rename the display name, kinda inefficient I guess.
            .build();
    }

    /**
     * Attempts to reload all locales into the language module.
     *
     * @since 2.0
     */
    @SuppressWarnings("ResultOfMethodCallIgnored")
    public void saveResources(final boolean force) {
        final String path = String.format("lang/%s", moduleName);
        final File folder = new File(plugin.getDataFolder(), path);
        if(!folder.exists()) folder.mkdirs();

        messages.clear();
        for(final Locale locale : Locale.values()) {
            final String fileName = String.format("%s_%s.properties", moduleName, locale.name());
            final File file = new File(folder, fileName);

            if(!file.exists() || force) {
                final InputStream stream = plugin.getResource(String.format("%s/%s", path, fileName));
                if(stream != null) plugin.saveResource(String.format("%s/%s", path, fileName), true);
            }

            try {
                if(file.exists()) reload(locale, file);
            } catch(final Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void reload(final @NotNull Locale locale, final File file) throws Exception {
        final Pattern pattern = Pattern.compile("^(.+)\\s*=\\s*(.*)\\\\{0,1}$");
        final Map<String, String> map = new HashMap<>();

        // To make sure we can even read Japanese, Chinese or whatever outside ASCII.
        final BufferedReader reader = new BufferedReader(new InputStreamReader(Files.newInputStream(file.toPath()), StandardCharsets.UTF_8));
        String key = "";
        StringJoiner joiner = new StringJoiner("\n");
        boolean inMultiLines = false;

        while(reader.ready()) {
            final String line = reader.readLine().trim();

            // In the multi lines sequence of the file, anything is counted towards the string.
            if(inMultiLines) {
                joiner.add(line.replaceAll("\\\\{0,1}$", ""));

                // More lines below, keep reading!
                if(line.endsWith("\\"))
                    continue;

                map.put(key, joiner.toString().trim());
                joiner = new StringJoiner("\n");
                inMultiLines = false;
                continue;
            }

            // Comments, skip it.
            if(line.startsWith("#"))
                continue;

            final Matcher matcher = pattern.matcher(line);
            if(!matcher.matches()) {
                ConsoleLogger.warnf("Weird syntax found at line \"%s\", ignoring.", line);
                continue;
            }

            key = matcher.group(1);
            if(line.endsWith("\\")) {
                inMultiLines = true;
                joiner = new StringJoiner("\n");

                final String toAdd = matcher.group(2).trim().replaceAll("\\\\{0,1}$", "");
                if(toAdd.length() > 0)
                    joiner.add(toAdd);
            } else {
                map.put(key, matcher.group(2).trim());
            }
        }

        reader.close();

        if(joiner.length() > 0)
            map.put(key, joiner.toString());

        this.messages.put(locale, map);
    }

}
