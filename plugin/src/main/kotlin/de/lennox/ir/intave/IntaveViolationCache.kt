package de.lennox.ir.intave

import de.lennox.ir.event.EventCallback
import de.lennox.ir.event.Events
import org.bukkit.entity.Player
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.event.player.PlayerQuitEvent

private lateinit var intaveViolationCache: IntaveViolationCache

fun Player.putViolation(violation: IntaveViolation) {
  val violationList = intaveViolationCache.cachedViolations[player] ?: return
  violationList.add(violation)
}

fun Player.violations(): MutableList<IntaveViolation>? {
  return intaveViolationCache.cachedViolations[player]
}

class IntaveViolationCache : Events {
  val cachedViolations = HashMap<Player, MutableList<IntaveViolation>>()

  init {
    intaveViolationCache = this
  }

  private val receiveJoin = EventCallback<PlayerJoinEvent> {
    cachedViolations.putIfAbsent(player, mutableListOf())
  }

  private val receiveQuit = EventCallback<PlayerQuitEvent> {
    cachedViolations.remove(player)
  }

}