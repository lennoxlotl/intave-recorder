package de.lennox.ir.command

import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

const val prefix = "§c§lRecorder §7»§r"

abstract class Command(
    private val permission: String = "",
    val name: String
) : CommandExecutor {
    private val subcommands = HashMap<String, (sender: CommandSender, args: Array<out String>) -> Unit>()

    /**
     * Dispatches the command internally
     *
     * @param sender The entity (or console) which dispatched the command
     * @param args The arguments supplied for the command dispatching
     */
    abstract fun dispatch(
        sender: CommandSender,
        args: Array<out String>
    )

    override fun onCommand(
        sender: CommandSender,
        command: Command,
        label: String,
        args: Array<out String>
    ): Boolean {
        // Check for possible subcommands
        if (subcommands.isNotEmpty() && args.isNotEmpty()) {
            val requestedSubcommand = args[0]
            // Search for a valid subcommand
            val subcommand = subcommands[requestedSubcommand] ?: run {
                sender.sendMessage("§cThis subcommand does not exist!")
                return true
            }
            // Dispatch the subcommand
            subcommand.invoke(sender, args.copyOfRange(1, args.size))
        }
        // Check for command permission
        if (permission.isNotEmpty()) {
            if (sender is Player && !sender.hasPermission(permission)) {
                sender.sendMessage("§cYou do not have enough permissions for this command!")
                return true
            }
        }
        // Dispatch the command
        dispatch(sender, args)
        return true
    }

    /**
     * Creates a new subcommand
     *
     * @param name The name of the subcommand, this also indicates how and when the command will be called
     * @param func The function which will be called on subcommand dispatch
     */
    fun subcommand(
        name: String,
        func: (sender: CommandSender, args: Array<out String>) -> Unit
    ) {
        subcommands[name] = func
    }
}