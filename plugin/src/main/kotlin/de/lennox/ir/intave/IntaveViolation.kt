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

  fun formatFor(player: Player): String {
    return "&c${player.name} &7violated &c$check &7vl: &c+$addedVl &7(&c$vlBefore&7->&c$vl&7)${if (message.isNotEmpty()) " &7details: &c$message" else ""} &7trust: ${trustFactor.coloredBaseName()}"
  }

}