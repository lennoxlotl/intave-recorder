package de.lennox.ir.web

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import de.lennox.ir.mongodb.MongoDriver
import de.lennox.ir.web.command.CommandDispatcher
import de.lennox.ir.web.command.CommandRepository
import de.lennox.ir.web.json.DelegateJsonConfig
import de.lennox.ir.web.json.JsonConfig
import io.javalin.Javalin
import io.javalin.apibuilder.ApiBuilder.get
import io.javalin.core.JavalinServer
import java.io.File
import java.util.*
import java.util.concurrent.Executors

val gson: Gson = GsonBuilder()
  .setPrettyPrinting()
  .create()

fun spigotPluginPath(pluginName: String, fileName: String): File =
  File("plugins/$pluginName/$fileName")

inline fun <reified T> jsonConfig(configPath: File, default: T): DelegateJsonConfig<T> =
  DelegateJsonConfig(configPath, T::class.java, default)

fun jsonConfig(configFile: File): JsonConfig =
  JsonConfig(configFile)

lateinit var webinterface: WebInterface

class WebInterface {
  private var jsonConfig by jsonConfig(
    File("config.json"),
    WIConfig(
      8080,
      "https://intave.de"
    )
  )
  private val baseHtmlFile: String = String(JavalinServer::class.java.getResourceAsStream("/index.html")?.readBytes()!!)
  private val baseLoginFile: String =
    String(JavalinServer::class.java.getResourceAsStream("/authorize.html")?.readBytes()!!)
  private val executor = Executors.newSingleThreadExecutor()

  val commandRepository = CommandRepository()
  var driver = MongoDriver("localhost", 27017)

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
      val passwordEntity = driver.passwordBy(password) ?: run {
        it.redirect(jsonConfig.redirectionLink)
        return@get
      }
      val fingerprint = UUID.randomUUID().toString().replace("-", "")
      it.cookieStore("rfp", fingerprint)
      driver.updatePasswordFingerprint(passwordEntity.passwordId, fingerprint)
      it.redirect("/log/$logId")
    }
    // Endpoint for viewing logs in the browser
    get("/log/:id") {
      val logId = it.pathParam("id")
      if (logId.isEmpty()) {
        it.redirect(jsonConfig.redirectionLink)
        return@get
      }
      val fingerprint = it.cookieStore<String>("rfp")
      if (fingerprint.isEmpty()) {
        val createdHtml = baseLoginFile
          .replace("%logId%", logId)
        it.html(createdHtml)
        return@get
      }
      driver.passwordByFingerprint(fingerprint) ?: run {
        val createdHtml = baseLoginFile
          .replace("%logId%", logId)
        it.html(createdHtml)
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
    webinterface = this
    app.start(jsonConfig.port)
    executor.submit { CommandDispatcher().run() }
  }
}

fun main() {
  WebInterface().start()
}