package de.lennox.ir.event

import de.lennox.ir.plugin
import org.bukkit.Bukkit
import org.bukkit.event.Event
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.plugin.EventExecutor
import java.lang.reflect.Field
import java.lang.reflect.ParameterizedType

class EventCallback<T : Event>(
  private val priority: EventPriority = EventPriority.NORMAL,
  private val receiver: T.() -> Unit,
)  {
  private val fallbackListener = object : Listener {}

  /**
   * Registers the callback by its field
   *
   * @param field The field which will be used to register the event callback
   */
  fun registerEvent(field: Field) {
    // Get the event type which should be registered
    val type = (field.genericType as ParameterizedType).actualTypeArguments[0]
    // Create a bukkit event executor
    val executor = EventExecutor { _, event ->
      @Suppress("UNCHECKED_CAST")
      receiver(event as T)
    }
    // Register the created executor in the bukkit api
    @Suppress("UNCHECKED_CAST")
    registerExecutor(type as Class<Event>, executor)
  }

  /**
   * Registers the callback in the bukkit api
   *
   * @param type The event type which will be registered
   * @param executor The event listener
   */
  private fun registerExecutor(
    type: Class<Event>,
    executor: EventExecutor
  ) {
    val pluginManager = Bukkit.getPluginManager()
    // Register the event in the bukkit api
    pluginManager.registerEvent(type, fallbackListener, priority, executor, plugin)
  }
}