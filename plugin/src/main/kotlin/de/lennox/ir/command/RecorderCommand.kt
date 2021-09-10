package de.lennox.ir.command

import de.lennox.ir.intave.violations
import org.bukkit.Bukkit
import org.bukkit.command.CommandSender

class RecorderCommand : Command("recorder.commands", "recorder") {
  init {
    subcommand("create") { sender, args ->
      if (args.isNotEmpty()) {
        val requestedPlayer = args[0]
        val player = Bukkit.getPlayer(requestedPlayer) ?: run {
          sender.sendMessage("$prefix §cThis player is not online!")
          return@subcommand
        }
        val violations = player.violations() ?: run {
          sender.sendMessage("$prefix §cUnable to receive the violations of the player!")
          return@subcommand
        }
        // Debug (remove later)
        println("Violations for ${player.name}:")
        violations.forEach {
          println(it.formatFor(player))
        }
        // TODO: Implement database and upload logs
        sender.sendMessage("$prefix §7Successfully uploaded the violation log of the player §d${player.name}§7.")
        violations.clear()
      } else {
        sender.sendMessage("$prefix §cPlease supply a player!")
      }
    }
  }

  override fun dispatch(sender: CommandSender, args: Array<out String>) {
  }

}