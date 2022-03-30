package dev.hawu.plugins.api.dsl.title

import dev.hawu.plugins.api.collections.MutableProperty
import dev.hawu.plugins.api.dsl.ScopeControlMarker
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

}
