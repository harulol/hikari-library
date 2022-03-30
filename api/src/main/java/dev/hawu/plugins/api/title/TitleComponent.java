package dev.hawu.plugins.api.title;

import org.bukkit.entity.Player;
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
    private final boolean dontWrap;
    private final long fadeIn;
    private final long stay;
    private final long fadeOut;

    /**
     * Creates a new title component, with 1 seconds of fade in,
     * 3 seconds of stay and 2 seconds of fade out.
     *
     * @param title    The title to send.
     * @param subtitle The subtitle to send.
     * @since 1.3
     */
    public TitleComponent(final @NotNull String title, final @NotNull String subtitle) {
        this(title, subtitle, 20, 60, 20);
    }

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
        this(title, subtitle, fadeIn, stay, fadeOut, false);
    }

    /**
     * Creates a new title component.
     *
     * @param title    The title to send.
     * @param subtitle The subtitle to send.
     * @param fadeIn   How long the title should fade in for, in ticks.
     * @param stay     How long the title should stay for, in ticks.
     * @param fadeOut  How long the title should fade out for, in ticks.
     * @param dontWrap Whether the text should be escaped.
     * @since 1.3
     */
    public TitleComponent(final @NotNull String title, final @NotNull String subtitle, final long fadeIn, final long stay, final long fadeOut, final boolean dontWrap) {
        this.title = title;
        this.subtitle = subtitle;
        this.fadeIn = fadeIn;
        this.stay = stay;
        this.fadeOut = fadeOut;
        this.dontWrap = dontWrap;
    }

    /**
     * Retrieves an instance of {@link TitleComponentBuilder}.
     *
     * @return Said instance.
     * @since 1.3
     */
    public static TitleComponentBuilder builder() {
        return new TitleComponentBuilder();
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

    /**
     * Whether the text should be escaped.
     *
     * @return Whether the text should be escaped.
     * @since 1.3
     */
    public boolean shouldNotWrap() {
        return dontWrap;
    }

    /**
     * Sends this title component to a player.
     *
     * @param player the player to send to
     * @since 1.6
     */
    public void send(final @NotNull Player player) {
        TitlePacketAdapter.getAdapter().send(player, this);
    }

    /**
     * Sends this title component to a player
     * as an action bar.
     *
     * @param player the player to send to
     * @since 1.6
     */
    public void sendActionBar(final @NotNull Player player) {
        TitlePacketAdapter.getAdapter().sendActionBar(player, this);
    }

}
