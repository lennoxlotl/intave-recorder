package de.lennox.ir.intave

import de.jpx3.intave.access.check.event.IntaveViolationEvent
import de.lennox.ir.event.EventCallback
import de.lennox.ir.event.Events

class IntaveAccessListener : Events {
    val receiveViolation = EventCallback<IntaveViolationEvent> {
        // Create a new violation entity with all necessary information
        val violation = IntaveViolation(
            violationLevelAfterViolation(),
            violationLevelBeforeViolation(),
            addedViolationPoints(),
            checkName(),
            playerAccess().trustFactor(),
            message()
        )
        // Push the violation into the player's violation cache
        player().putViolation(violation)
    }
}