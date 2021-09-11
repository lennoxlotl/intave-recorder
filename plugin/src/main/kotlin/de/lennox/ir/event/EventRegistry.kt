package de.lennox.ir.event

interface Events

object EventRegistry {
  /**
   * Registers multiple event listeners in the Bukkit API
   *
   * @param events List of event listeners which will be registered
   */
  fun registerEventsIn(vararg events: Events) {
    events.forEach { event ->
      // Loop through all fields in the event listener class
      for (field in event::class.java.declaredFields) {
        field.isAccessible = true
        try {
          // If the field is an event callback register it as listener
          (field[event] as? EventCallback<*>)?.registerEvent(field)
        } catch (exception: Exception) {
          throw IllegalStateException("Error registering ${field.name} in ${event.javaClass.name}")
        }
      }
    }
  }
}