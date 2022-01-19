package dev.hawu.plugins.api.dsl.chat

import dev.hawu.plugins.api.chat.TextClickEvent
import dev.hawu.plugins.api.chat.TextComponentPart
import dev.hawu.plugins.api.chat.TextFormattingOption
import dev.hawu.plugins.api.chat.TextHoverEvent
import dev.hawu.plugins.api.dsl.ScopeControlMarker

/**
 * The specification class for configuring and creating
 * a [TextComponentPart] in a DSL.
 * @since 1.5
 */
@Suppress("MemberVisibilityCanBePrivate")
@ScopeControlMarker
class TextComponentPartSpec {

    /**
     * The text to be displayed.
     */
    var text: String = ""

    /**
     * The color of the text.
     */
    var color: String? = null

    /**
     * The font to render the text in.
     */
    var font: String? = null

    /**
     * The boldness of the text.
     */
    var bold: Boolean? = null

    /**
     * The italicness of the text.
     */
    var italic: Boolean? = null

    /**
     * The underline of the text.
     */
    var underline: Boolean? = null

    /**
     * The strikethrough of the text.
     */
    var strikethrough: Boolean? = null

    /**
     * The obfuscation of the text.
     */
    var obfuscated: Boolean? = null

    /**
     * The insertion of the text.
     */
    var insertion: String? = null

    /**
     * The click event of the text.
     */
    var clickEvent: TextClickEvent? = null

    /**
     * The hover event of the text.
     */
    var hoverEvent: TextHoverEvent? = null

    /**
     * The extra data of the text.
     */
    private val extraData: MutableList<TextComponentPart> = mutableListOf()

    /**
     * Adds a [TextComponentPart] to the extra data using
     * the parsed [spec].
     */
    fun extra(spec: TextComponentPartSpec.() -> Unit) {
        extraData.add(TextComponentPartSpec().apply(spec).build())
    }

    private fun Boolean?.toTextFormattingOption() = when(this) {
        null -> TextFormattingOption.INHERIT
        false -> TextFormattingOption.FALSE
        true -> TextFormattingOption.TRUE
    }

    internal fun build(): TextComponentPart = TextComponentPart(
        text, extraData, color, font,
        bold.toTextFormattingOption(),
        italic.toTextFormattingOption(),
        underline.toTextFormattingOption(),
        strikethrough.toTextFormattingOption(),
        obfuscated.toTextFormattingOption(),
        insertion, clickEvent, hoverEvent,
    )

}
