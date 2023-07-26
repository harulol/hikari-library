package dev.hawu.plugins.kapi

import java.text.DecimalFormat
import java.util.*

val formatter = DecimalFormat()

/**
 * Converts a string to a colored string.
 */
val String.colored: String
    get() = this.replace('&', '§')

/**
 * Converts a string to a colored string with format.
 */
fun String.colorf(vararg args: Any?): String = this.format(args)

/**
 * Converts a string to a UUID if valid.
 */
fun String.toUUID(): UUID? = try {
    UUID.fromString(this)
} catch (e: IllegalArgumentException) {
    null
}

/**
 * Chops a string into a list of strings.
 */
fun String.chop(threshold: Int = 30): List<String> = this.split(" ").chop(threshold)

/**
 * Chops a list of strings into an organized list of strings.
 */
tailrec fun List<String>.chop(threshold: Int = 30, acc: String = "", result: List<String> = emptyList()): List<String> {
    if (this.isEmpty())
        return if (acc.isNotBlank()) result + acc else result

    val head = this.first()
    val compare = head.length + acc.length + 1 > threshold
    val newAcc = if (compare || acc.isEmpty()) head else "$acc $head"
    return this.drop(1).chop(threshold, newAcc, if (compare) result + acc else result)
}

/**
 * Returns true if [this] string equals any of the [strings].
 */
fun String.equalsAny(vararg strings: String): Boolean = strings.any { this.equals(it, false) }

/**
 * Returns true if [this] string equals any of the [strings] ignoring case.
 */
fun String.equalsAnyIgnoreCase(vararg strings: String): Boolean = strings.any { this.equals(it, true) }

/**
 * Returns a formatted [String] of a [number][this].
 */
fun Number.format(): String = formatter.format(this)

/**
 * Fills a string with the given arguments, the recursive implementation.
 */
tailrec fun String.fill(args: List<Pair<*, *>>): String {
    if (args.isEmpty())
        return this
    return this.replace("%${args[0].first}%", args[0].second.toString()).fill(args.drop(1))
}

/**
 * Fills a string with the given arguments.
 */
fun String.fill(vararg args: Pair<*, *>): String {
    return this.fill(args.toList())
}
