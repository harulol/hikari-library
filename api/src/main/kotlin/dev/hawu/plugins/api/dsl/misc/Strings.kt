package dev.hawu.plugins.api.dsl.misc

import dev.hawu.plugins.api.Strings
import java.text.DecimalFormat

private val formatter = DecimalFormat("#.###,##")

/**
 * Colorize the string. Does the same thing
 * as [Strings.color].
 */
fun String.color() = Strings.color(this)

/**
 * Fills the string with the given placeholders.
 * Does the same thing as [Strings.fillPlaceholders].
 */
fun String.fillPlaceholders(vararg args: Pair<Any, Any>) = Strings.fillPlaceholders(this, *args.map { it.toLibrary() }.toTypedArray())

/**
 * Formats a number with digit separators and
 * 2 decimal places. Does the same thing as [Strings.format].
 */
fun Number.format() = formatter.format(this)

/**
 * Attempts to convert a [String] to a [java.util.UUID].
 * Returns null if the conversion failed.
 */
fun String.toUUID() = Strings.toUUIDOrNull(this)
