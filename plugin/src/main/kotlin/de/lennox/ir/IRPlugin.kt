package de.lennox.ir

import de.lennox.ir.command.CommandRegistry
import de.lennox.ir.command.RecorderCommand
import de.lennox.ir.event.EventRegistry
import de.lennox.ir.intave.IntaveAccessListener
import de.lennox.ir.intave.IntaveViolationCache
import org.bukkit.plugin.java.JavaPlugin

lateinit var plugin: IRPlugin;

class IRPlugin : JavaPlugin() {

  override fun onEnable() {
    println("Enabling Intave Recorder")
    plugin = this
    registerEvents()
    registerCommands()
  }

  private fun registerEvents() {
    EventRegistry.registerEventsIn(
      IntaveViolationCache(),
      IntaveAccessListener()
    )
  }

  private fun registerCommands() {
    CommandRegistry.registerCommands(
      RecorderCommand()
    )
  }

}