package dev.hawu.plugins.api.dsl.commands

import dev.hawu.plugins.api.commands.CommandArgument
import dev.hawu.plugins.api.commands.CommandLine
import dev.hawu.plugins.api.commands.CommandSource
import dev.hawu.plugins.api.dsl.ScopeControlMarker
import org.bukkit.entity.Player

/**
 * Specification to be executed on command.
 * @property sender the source of the command
 * @property args the arguments sent
 */
@ScopeControlMarker
class CommandRunSpec internal constructor(
    val sender: CommandSource,
    val args: CommandArgument,
    private val cli: CommandLine?,
) {

    /**
     * The player that sent the command,
     * if available.
     */
    val player: Player
        get() = sender.player

    /**
     * Stops the execution of this specification :)
     */
    val stop: Unit
        get() = throw InterruptedException("Stop command called.")

    /**
     * Retrieves the parsed arguments.
     * Requires the CLI property to be set beforehand.
     */
    val parsedArguments: Pair<CommandArgument, Map<String, List<String>>>
        get() {
            val lib = args.withCLI(cli).parse()
            return lib.first to lib.second
        }

}
