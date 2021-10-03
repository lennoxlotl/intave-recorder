package de.lennox.ir.command

import de.lennox.ir.plugin

object CommandRegistry {
    /**
     * Registers multiple commands in the Bukkit API
     *
     * @param commands Vararg of all commands which should be registered
     */
    fun registerCommands(vararg commands: Command) {
        commands.forEach { command ->
            plugin.getCommand(command.name).executor = command
        }
    }
}