package de.lennox.ir.event

interface Events

object EventRegistry {
  fun registerEventsIn(vararg events: Events) {
    events.forEach { event ->
      for (field in event::class.java.declaredFields) {
        field.isAccessible = true
        try {
          (field[event] as? EventCallback<*>)?.registerEvent(field)
        } catch (exception: Exception) {
          throw IllegalStateException("Error registering ${field.name} in ${event.javaClass.name}")
        }
      }
    }
  }
}