package dev.hawu.plugins.api.title;

import org.jetbrains.annotations.NotNull;

/**
 * A set of title and subtitle with configurations
 * for display timings.
 *
 * @since 1.0
 */
public final class TitleComponent {

    private final String title;
    private final String subtitle;
    private final long fadeIn;
    private final long stay;
    private final long fadeOut;

    /**
     * Creates a new title component, ready to be sent.
     *
     * @param title    The title to send.
     * @param subtitle The subtitle to send.
     * @param fadeIn   How long the title should fade in for, in ticks.
     * @param stay     How long the title should stay for, in ticks.
     * @param fadeOut  How long the title should fade out for, in ticks.
     * @since 1.0
     */
    public TitleComponent(final @NotNull String title, final @NotNull String subtitle, final long fadeIn, final long stay, final long fadeOut) {
        this.title = title;
        this.subtitle = subtitle;
        this.fadeIn = fadeIn;
        this.stay = stay;
        this.fadeOut = fadeOut;
    }

    /**
     * Gets the bare title of this component.
     *
     * @return The bare title, not colorized.
     * @since 1.0
     */
    @NotNull
    public String getTitle() {
        return title;
    }

    /**
     * Gets the bare subtitle of this component.
     *
     * @return The bare subtitle, not colorized.
     * @since 1.0
     */
    @NotNull
    public String getSubtitle() {
        return subtitle;
    }

    /**
     * Gets the amount of ticks the titles should spend
     * fading in.
     *
     * @return The amount of ticks.
     * @since 1.0
     */
    public long getFadeIn() {
        return fadeIn;
    }

    /**
     * Gets the amount of ticks the titles should stay for.
     *
     * @return The amount of ticks.
     * @since 1.0
     */
    public long getStay() {
        return stay;
    }

    /**
     * Gets the amount of ticks the titles should spend
     * fading out.
     *
     * @return The amount of ticks.
     * @since 1.0
     */
    public long getFadeOut() {
        return fadeOut;
    }

}
