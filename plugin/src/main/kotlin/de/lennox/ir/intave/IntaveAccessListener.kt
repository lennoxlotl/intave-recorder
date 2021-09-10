package de.lennox.ir.intave

import de.jpx3.intave.access.check.event.IntaveViolationEvent
import de.lennox.ir.event.EventCallback
import de.lennox.ir.event.Events

class IntaveAccessListener : Events {
  val receiveViolation = EventCallback<IntaveViolationEvent> {
    val violation = IntaveViolation(
      violationLevelAfterViolation(),
      violationLevelBeforeViolation(),
      addedViolationPoints(),
      checkName(),
      playerAccess().trustFactor(),
      message()
    )
    player().putViolation(violation)
  }
}