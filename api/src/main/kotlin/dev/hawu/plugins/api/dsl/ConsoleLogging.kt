package dev.hawu.plugins.api.dsl

import dev.hawu.plugins.api.misc.ConsoleLogger

/**
 * Log a [message][s] to the console, default to white.
 */
fun log(s: String) {
    ConsoleLogger.log(s)
}

/**
 * Log a [message][s] to the console, default to green.
 */
fun fine(s: String) {
    ConsoleLogger.fine(s)
}

/**
 * Log a [message][s] to the console, default to yellow.
 */
fun warn(s: String) {
    ConsoleLogger.warn(s)
}

/**
 * Log a [message][s] to the console, default to red.
 */
fun severe(s: String) {
    ConsoleLogger.severe(s)
}
