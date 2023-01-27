package dev.hawu.plugins.api.gui.templates;

import dev.hawu.plugins.api.collections.tuples.Pair;
import dev.hawu.plugins.api.gui.GuiComponent;
import dev.hawu.plugins.api.i18n.LanguageModule;
import dev.hawu.plugins.api.i18n.Locale;
import dev.hawu.plugins.api.user.ExtendedUser;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.Collections;

/**
 * Represents a component that can be translated using
 * a language module.
 *
 * @param <S> the type of the state of this component
 * @since 2.0
 */
public abstract class TranslatableComponent<S> extends GuiComponent<S> {

    private final LanguageModule module;
    private final String key;
    private final Locale locale;

    /**
     * Creates a component that can be translated.
     *
     * @param module the module
     * @param key    the key for the translations
     * @param locale the locale to translate with
     * @since 2.0
     */
    public TranslatableComponent(final @NotNull LanguageModule module, final @NotNull String key, final @NotNull Locale locale) {
        this.module = module;
        this.key = key;
        this.locale = locale;
    }

    /**
     * Creates a component that can be translated.
     *
     * @param module the module
     * @param key    the key for translations
     * @param user   the user to translate for
     * @since 2.0
     */
    public TranslatableComponent(final @NotNull LanguageModule module, final @NotNull String key, final @NotNull ExtendedUser user) {
        this(module, key, user.getLocale());
    }

    /**
     * Retrieves the template item stack as the base for translations.
     *
     * @return the template
     * @since 2.0
     */
    public abstract ItemStack getTemplate();

    /**
     * Retrieves the collection of replacements to pass in for translations.
     *
     * @return the collection of replacements
     * @since 2.0
     */
    public Collection<Pair<?, ?>> getReplacements() {
        return Collections.emptyList();
    }

    @Override
    public ItemStack render() {
        return module.translateItem(locale, getTemplate(), key, getReplacements().toArray(new Pair[0]));
    }

}
