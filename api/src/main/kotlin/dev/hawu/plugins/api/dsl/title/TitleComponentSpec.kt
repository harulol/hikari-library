package dev.hawu.plugins.api.dsl.title

import dev.hawu.plugins.api.collections.MutableProperty
import dev.hawu.plugins.api.dsl.ScopeControlMarker
import dev.hawu.plugins.api.dsl.time.toMillis
import dev.hawu.plugins.api.title.TitleComponent

/**
 * Specification for building a title component.
 */
@ScopeControlMarker
class TitleComponentSpec internal constructor() {

    private var _dontWrap = false

    /**
     * The main title of this title.
     */
    val title = MutableProperty.of<String>("")

    /**
     * The subtitle of this title.
     */
    val subtitle = MutableProperty.of<String>("")

    /**
     * Set the animations of this title.
     */
    val animations: ITitleAnimationsOptions
        get() = TitleAnimationsOptions()

    /**
     * The time in ticks to fade in the title.
     */
    val fadeIn = MutableProperty.of<Long>(20L)

    /**
     * The time in ticks for the title to stay on screen.
     */
    val stay = MutableProperty.of<Long>(60L)

    /**
     * The time in ticks to fade out the title.
     */
    val fadeOut = MutableProperty.of<Long>(20L)

    /**
     * The packet adapter should not wrap this title
     * in `{"text":"..."}`.
     */
    val dontWrap: Unit
        get() {
            _dontWrap = true
        }

    internal fun build() = TitleComponent(title.get(), subtitle.get(), fadeIn.get(), stay.get(), fadeOut.get(), _dontWrap)

    /**
     * For chaining infix calls to set animations.
     */
    interface ITitleAnimationsOptions {

        /**
         * Sets the fade in time in ticks.
         */
        infix fun fadeIn(time: Long): ITitleAnimationsOptions

        /**
         * Sets the fade in time.
         */
        infix fun fadeIn(time: String): ITitleAnimationsOptions

        /**
         * Sets the stay time in ticks.
         */
        infix fun stay(time: Long): ITitleAnimationsOptions

        /**
         * Sets the stay time.
         */
        infix fun stay(time: String): ITitleAnimationsOptions

        /**
         * Sets the fade out time in ticks.
         */
        infix fun fadeOut(time: Long): ITitleAnimationsOptions

        /**
         * Sets the fade out time.
         */
        infix fun fadeOut(time: String): ITitleAnimationsOptions

    }

    private inner class TitleAnimationsOptions : ITitleAnimationsOptions {
        override fun fadeIn(time: Long): ITitleAnimationsOptions {
            fadeIn.set(time)
            return this
        }

        override fun fadeIn(time: String): ITitleAnimationsOptions {
            fadeIn.set(time.toMillis().toLong() / 20)
            return this
        }

        override fun stay(time: Long): ITitleAnimationsOptions {
            stay.set(time)
            return this
        }

        override fun stay(time: String): ITitleAnimationsOptions {
            stay.set(time.toMillis().toLong() / 20)
            return this
        }

        override fun fadeOut(time: Long): ITitleAnimationsOptions {
            fadeOut.set(time)
            return this
        }

        override fun fadeOut(time: String): ITitleAnimationsOptions {
            fadeOut.set(time.toMillis().toLong() / 20)
            return this
        }
    }

}
