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

  fun registerEvent(field: Field) {
    val type = (field.genericType as ParameterizedType).actualTypeArguments[0]
    val executor = EventExecutor { _, event ->
      @Suppress("UNCHECKED_CAST")
      receiver(event as T)
    }
    @Suppress("UNCHECKED_CAST")
    registerExecutor(type as Class<Event>, executor)
  }

  private fun registerExecutor(type: Class<Event>, executor: EventExecutor) {
    val pluginManager = Bukkit.getPluginManager()
    pluginManager.registerEvent(type, fallbackListener, priority, executor, plugin)
  }
}