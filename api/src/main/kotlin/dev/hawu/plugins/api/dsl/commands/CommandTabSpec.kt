package dev.hawu.plugins.api.dsl.commands

import dev.hawu.plugins.api.commands.CommandArgument
import dev.hawu.plugins.api.commands.CommandSource
import dev.hawu.plugins.api.dsl.ScopeControlMarker
import org.bukkit.entity.Player

/**
 * Specification to be executed when a user presses tab.
 * @property sender the sender of the command
 * @property args the arguments of the command
 */
@ScopeControlMarker
class CommandTabSpec internal constructor(
    val sender: CommandSource,
    val args: CommandArgument,
) {

    /**
     * The player that sent the command,
     * if available.
     */
    val player: Player
        get() = sender.player

    /**
     * Filters the list of returning arguments to only those that
     * start with the text in the last typed argument.
     */
    fun List<String>.filterStartsWith() = filter { it.startsWith(args.lastOrNull() ?: "") }

}
