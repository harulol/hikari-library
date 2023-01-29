package dev.hawu.plugins.api.items;

import dev.hawu.plugins.api.adapters.PluginAdapter;
import dev.hawu.plugins.api.i18n.Locale;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

/**
 * Represents a utility class that creates items
 * based on locales.
 * <p>
 * Most items present within the library, such as pagination,
 * component templates are in English, this utility object is
 * to make those but in other languages.
 *
 * @since 2.0
 */
public final class ItemCreator {

    private ItemCreator() {
        // Prevent instantiation
    }

    /**
     * Translates the item to go back one page
     * in paginations.
     *
     * @param locale the locale to translate in
     * @return the item template
     * @since 2.0
     */
    public static ItemStack getPreviousButton(final @NotNull Locale locale) {
        return PluginAdapter.getModule().translateItem(locale, new ItemStack(Material.ARROW), "previous-button");
    }

    /**
     * Translates the item to go forward one page
     * in paginations.
     *
     * @param locale the locale to translate in
     * @return the item template
     * @since 2.0
     */
    public static ItemStack getNextButton(final @NotNull Locale locale) {
        return PluginAdapter.getModule().translateItem(locale, new ItemStack(Material.ARROW), "next-button");
    }

    /**
     * Translates the item to close the GUI.
     *
     * @param locale the locale to translate in
     * @return the item template
     * @since 2.0
     */
    public static ItemStack getCloseButton(final @NotNull Locale locale) {
        return PluginAdapter.getModule().translateItem(locale, new ItemStack(Material.BARRIER), "close-button");
    }

    /**
     * Translates the item to go back to the previous
     * menu type.
     *
     * @param locale the locale
     * @return the item template
     * @since 2.0
     */
    public static ItemStack getBackButton(final @NotNull Locale locale) {
        return PluginAdapter.getModule().translateItem(locale, new ItemStack(Material.SIGN), "back-button");
    }

    /**
     * Translates the item to filter out contents in a pagination.
     *
     * @param locale the locale
     * @return the item template
     * @since 2.0
     */
    public static ItemStack getFilterButton(final @NotNull Locale locale) {
        return PluginAdapter.getModule().translateItem(locale, new ItemStack(Material.COMPASS), "filter-button");
    }

}
