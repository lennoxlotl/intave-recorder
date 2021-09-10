package de.lennox.ir.command

import de.lennox.ir.plugin

object CommandRegistry {
  fun registerCommands(vararg commands: Command) {
    commands.forEach { command ->
      plugin.getCommand(command.name).executor = command
    }
  }
}