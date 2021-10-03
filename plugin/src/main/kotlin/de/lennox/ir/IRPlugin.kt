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
        // Register all necessary objects
        registerEvents()
        registerCommands()
        // Initialize the driver
        // TODO: Config for the driver
        driver = MongoDriver("localhost", 27017)
    }

    private fun registerEvents() {
        // Register all events in the Bukkit API
        EventRegistry.registerEventsIn(
            IntaveViolationCache(),
            IntaveAccessListener()
        )
    }

    private fun registerCommands() {
        // Register all commands in the Bukkit API
        CommandRegistry.registerCommands(
            RecorderCommand()
        )
    }
}