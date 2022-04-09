package dev.hawu.plugins.api.dsl.commands

import dev.hawu.plugins.api.collections.MutableProperty
import dev.hawu.plugins.api.commands.AbstractCommandClass
import dev.hawu.plugins.api.commands.CommandArgument
import dev.hawu.plugins.api.commands.CommandLine
import dev.hawu.plugins.api.commands.CommandSource
import dev.hawu.plugins.api.commands.SenderType
import dev.hawu.plugins.api.dsl.ScopeControlMarker
import org.bukkit.plugin.java.JavaPlugin

/**
 * Specification for an [AbstractCommandClass].
 */
@ScopeControlMarker
open class CommandSpec(spec: CommandSpec.() -> Unit) {

    private var commandClass: AbstractCommandClass
    private var runSpec: CommandRunSpec.() -> Unit = {}
    private var tabSpec: CommandTabSpec.() -> List<String>? = { null }
    private var plugin: JavaPlugin? = null
    private val senders = mutableSetOf<SenderType>()
    private val subcommands = mutableListOf<AbstractCommandClass>()

    val name = MutableProperty.of<String>(null)

    val parser = MutableProperty.of<CommandLine>(null)

    val permission = MutableProperty.of<String>(null)

    val permissionMessage = MutableProperty.of<String>(PERMISSION_MESSAGE)

    val wrongSenderMessage = MutableProperty.of<String>(WRONG_SENDER_MESSAGE)

    /**
     * Allows certain types of senders.
     */
    fun allow(spec: CommandAllowSenderSpec.() -> Unit) {
        CommandAllowSenderSpec(senders).spec()
    }

    /**
     * Registers a new sub-command.
     */
    fun subcommand(spec: CommandSpec.() -> Unit) {
        val subcommand = CommandSpec(spec)
        subcommands.add(subcommand.commandClass)
    }

    /**
     * Registers a new sub-command.
     */
    fun subcommand(cls: CommandSpec) {
        subcommands.add(cls.commandClass)
    }

    /**
     * Registers a new sub-command.
     */
    fun subcommand(cls: AbstractCommandClass) {
        subcommands.add(cls)
    }

    /**
     * Registers this command as a root command
     * to the [plugin][pl].
     */
    fun register(pl: JavaPlugin) {
        this.plugin = pl
    }

    /**
     * Calls when the command is executed by the user
     * who satisfied all the predicates.
     */
    fun run(spec: CommandRunSpec.() -> Unit) {
        this.runSpec = spec
    }

    /**
     * Calls when the command's tab completions are
     * requested by a user.
     */
    fun tab(spec: CommandTabSpec.() -> List<String>?) {
        this.tabSpec = spec
    }

    init {
        this.spec()
        commandClass = object : AbstractCommandClass(name.get()) {
            init {
                permission = this@CommandSpec.permission.orNull()
                permissionMessage = this@CommandSpec.permissionMessage.get()
                badSenderMessage = wrongSenderMessage.get()
                parser.ifPresent { setParser(it) }

                senders.forEach { allow(it) }
                subcommands.forEach { bind(it) }

                if(plugin != null) register(plugin!!)
            }

            override fun run(sender: CommandSource, args: CommandArgument) {
                CommandRunSpec(sender, args, parser.orNull()).runSpec()
            }

            override fun tab(sender: CommandSource, args: CommandArgument): List<String>? {
                return CommandTabSpec(sender, args).tabSpec()
            }
        }
    }

    companion object {

        private const val PERMISSION_MESSAGE = "&cI'm sorry, but you do not have permission to perform this command."
        private const val WRONG_SENDER_MESSAGE = "&cThis command was not tailored for your type of sender."

    }

}
