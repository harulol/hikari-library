package dev.hawu.plugins.api.title;

import org.jetbrains.annotations.NotNull;

/**
 * Represents a builder to build a {@link TitleComponent}
 * with named arguments.
 *
 * @since 1.3
 */
public final class TitleComponentBuilder {

    private String title;
    private String subtitle;
    private boolean dontWrap;
    private long fadeIn = 20;
    private long stay = 60;
    private long fadeOut = 20;

    TitleComponentBuilder() {}

    /**
     * Configures the title of the component.
     *
     * @param title The title of the component.
     * @return The builder.
     * @since 1.3
     */
    @NotNull
    public TitleComponentBuilder title(@NotNull String title) {
        this.title = title;
        return this;
    }

    /**
     * Configures the subtitle of the component.
     *
     * @param subtitle The subtitle of the component.
     * @return The builder.
     * @since 1.3
     */
    @NotNull
    public TitleComponentBuilder subtitle(@NotNull String subtitle) {
        this.subtitle = subtitle;
        return this;
    }

    /**
     * Configures all the animation timings of the component.
     *
     * @param fadeIn  The ticks taken to fade in.
     * @param stay    The ticks taken to stay on screen.
     * @param fadeOut The ticks taken to fade out.
     * @return The builder.
     * @since 1.3
     */
    @NotNull
    public TitleComponentBuilder timing(long fadeIn, long stay, long fadeOut) {
        this.fadeIn = fadeIn;
        this.stay = stay;
        this.fadeOut = fadeOut;
        return this;
    }

    /**
     * Configures how long the component fades in for.
     *
     * @param fadeIn The ticks taken to fade in.
     * @return The builder.
     * @since 1.3
     */
    @NotNull
    public TitleComponentBuilder fadeIn(long fadeIn) {
        this.fadeIn = fadeIn;
        return this;
    }

    /**
     * Configures how long the component stays on screen for.
     *
     * @param stay The ticks taken to stay.
     * @return The builder.
     * @since 1.3
     */
    @NotNull
    public TitleComponentBuilder stay(long stay) {
        this.stay = stay;
        return this;
    }

    /**
     * Configures how long the component fades out for.
     *
     * @param fadeOut The ticks taken to fade out.
     * @return The builder.
     * @since 1.3
     */
    @NotNull
    public TitleComponentBuilder fadeOut(long fadeOut) {
        this.fadeOut = fadeOut;
        return this;
    }

    /**
     * Configures the component's data to not be wrapped via
     * {@link org.apache.commons.lang.StringEscapeUtils}.
     *
     * @return The builder.
     * @since 1.3
     */
    @NotNull
    public TitleComponentBuilder noWraps() {
        this.dontWrap = true;
        return this;
    }

    /**
     * Builds the title component from set values.
     *
     * @return The built component.
     * @since 1.3
     */
    @NotNull
    public TitleComponent build() {
        return new TitleComponent(title, subtitle, fadeIn, stay, fadeOut, dontWrap);
    }

}
