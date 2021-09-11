package de.lennox.ir.intave

import de.jpx3.intave.access.player.trust.TrustFactor
import org.bukkit.entity.Player

data class IntaveViolation(
  val vl: Double,
  val vlBefore: Double,
  val addedVl: Double,
  val check: String,
  val trustFactor: TrustFactor,
  val message: String
) {
  /**
   * Formats the recorded violation as a string
   *
   * @param player The player which owns the violation
   * @return The formatted violation as string
   */
  fun formatFor(player: Player): String {
    return "&c${player.name} &7violated &b$check &7vl: &c+$addedVl &7($vlBefore->$vl)${if (message.isNotEmpty()) " &7details: $message" else ""} &7trust: ${trustFactor.coloredBaseName()}"
  }
}