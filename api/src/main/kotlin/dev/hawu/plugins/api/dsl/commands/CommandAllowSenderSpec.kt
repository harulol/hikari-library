package dev.hawu.plugins.api.dsl.commands

import dev.hawu.plugins.api.commands.SenderType
import dev.hawu.plugins.api.dsl.ScopeControlMarker

/**
 * Specification to allow specific types of senders
 * to use the command.
 */
@ScopeControlMarker
class CommandAllowSenderSpec internal constructor(private val set: MutableSet<SenderType>) {

    /**
     * Allow players to use the command.
     */
    val players: ICommandSenderOption
        get() {
            set.add(SenderType.PLAYER)
            return CommandSenderOption
        }

    /**
     * Allow console to use the command.
     */
    val console: ICommandSenderOption
        get() {
            set.add(SenderType.CONSOLE)
            return CommandSenderOption
        }

    /**
     * Allow command blocks to use the command.
     */
    val blocks: ICommandSenderOption
        get() {
            set.add(SenderType.BLOCK)
            return CommandSenderOption
        }

    /**
     * Allows proxied players to use the command
     * (usually via /execute).
     */
    val proxiedPlayers: ICommandSenderOption
        get() {
            set.add(SenderType.PROXIED)
            return CommandSenderOption
        }

    /**
     * Allows remote console to use the command.
     */
    val remoteConsole: ICommandSenderOption
        get() {
            set.add(SenderType.REMOTE_CONSOLE)
            return CommandSenderOption
        }

    /**
     * Allows any sender to use the command.
     */
    val any: Unit
        get() {
            set.addAll(SenderType.values())
        }

    /**
     * Sealed interface for chaining options.
     */
    sealed interface ICommandSenderOption {
        infix fun and(other: ICommandSenderOption) = other
    }

    private object CommandSenderOption : ICommandSenderOption

}
