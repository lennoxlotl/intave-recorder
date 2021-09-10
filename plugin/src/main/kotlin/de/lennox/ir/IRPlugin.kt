package de.lennox.ir

import de.lennox.ir.command.CommandRegistry
import de.lennox.ir.command.RecorderCommand
import de.lennox.ir.event.EventRegistry
import de.lennox.ir.intave.IntaveAccessListener
import de.lennox.ir.intave.IntaveViolationCache
import de.lennox.ir.mongodb.MongoDriver
import org.bukkit.plugin.java.JavaPlugin

lateinit var plugin: IRPlugin;

class IRPlugin : JavaPlugin() {

  lateinit var driver: Driver

  override fun onEnable() {
    println("Enabling Intave Recorder")
    plugin = this
    registerEvents()
    registerCommands()
    driver = MongoDriver("localhost", 27017)
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