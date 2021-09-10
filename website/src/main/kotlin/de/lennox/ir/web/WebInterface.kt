package de.lennox.ir.web


import com.google.gson.Gson
import com.google.gson.GsonBuilder
import de.lennox.ir.mongodb.MongoDriver
import de.lennox.ir.web.json.DelegateJsonConfig
import de.lennox.ir.web.json.JsonConfig
import io.javalin.Javalin
import io.javalin.apibuilder.ApiBuilder.get
import io.javalin.core.JavalinServer
import java.io.File


val gson: Gson = GsonBuilder()
  .setPrettyPrinting()
  .create()

fun spigotPluginPath(pluginName: String, fileName: String): File =
  File("plugins/$pluginName/$fileName")

inline fun <reified T> jsonConfig(configPath: File, default: T): DelegateJsonConfig<T> =
  DelegateJsonConfig(configPath, T::class.java, default)

fun jsonConfig(configFile: File): JsonConfig =
  JsonConfig(configFile)

class WebInterface {
  private var jsonConfig by jsonConfig(
    File("config.json"),
    WIConfig(
      8080,
      "https://intave.de"
    )
  )
  private val baseHtmlFile: String = String(JavalinServer::class.java.getResourceAsStream("/index.html")?.readBytes()!!)
  private var driver = MongoDriver("localhost", 27017)

  private val app = Javalin.create().routes {
    // Default endpoint
    get("/") {
      it.redirect(jsonConfig.redirectionLink)
    }
    get("/authorize/:password/:id") {
      val logId = it.pathParam("id")
      val password = it.pathParam("password")
      if (logId.isEmpty() || password.isEmpty()) {
        it.redirect(jsonConfig.redirectionLink)
        return@get
      }
      // TODO: Handle passwords
    }
    // Endpoint for viewing logs in the browser
    get("/log/:id") {
      // TODO: Password authorization through mongodb
      val logId = it.pathParam("id")
      if (logId.isEmpty()) {
        it.redirect(jsonConfig.redirectionLink)
        return@get
      }
      val log = driver.recordBy(logId) ?: run {
        it.redirect(jsonConfig.redirectionLink)
        return@get
      }
      val logs = log.logs
        .joinToString("<br>") { entry ->
          entry.replace("§", "&")
        }
      val createdHtml = baseHtmlFile
        .replace("%logId%", log.recordId)
        .replace("%log%", logs)
        .replace("%playerName%", log.owner)
        .replace("%playerUUID%", log.uuid)
      it.html(createdHtml)
    }
  }

  fun start() {
    app.start(jsonConfig.port)
  }
}

fun main() {
  WebInterface().start()
}