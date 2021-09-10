package de.lennox.ir.command

import de.lennox.ir.entity.RecordEntity
import de.lennox.ir.intave.violations
import de.lennox.ir.plugin
import org.bukkit.Bukkit
import org.bukkit.command.CommandSender
import kotlin.random.Random

private const val ALPHANUMERIC_REGEX = "[a-zA-Z0-9]+";
private val charPool : List<Char> = ('a'..'z') + ('A'..'Z') + ('0'..'9')

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
        val violationsAsStringList = violations.map { it.formatFor(player) }
        val databaseRecord = RecordEntity(
          randomId(8),
          violationsAsStringList,
          player.uniqueId.toString(),
          player.name
        )
        plugin.driver.pushRecord(databaseRecord)
        sender.sendMessage("$prefix §7Successfully uploaded the violation log of the player §d${player.name}§7.")
        violations.clear()
      } else {
        sender.sendMessage("$prefix §cPlease supply a player!")
      }
    }
  }

  override fun dispatch(sender: CommandSender, args: Array<out String>) {
  }

  private fun randomId(length: Int): String {
    return (1..length)
      .map { Random.nextInt(0, charPool.size) }
      .map(charPool::get)
      .joinToString("")
  }

}