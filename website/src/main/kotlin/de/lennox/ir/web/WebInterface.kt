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
            "https://intave.de",
            MongoDBConfig(
                "",
                0,
                false,
                "",
                "",
                ""
            )
        )
    )
    private val baseHtmlFile: String =
        String(JavalinServer::class.java.getResourceAsStream("/index.html")?.readBytes()!!)
    private val baseLoginFile: String =
        String(JavalinServer::class.java.getResourceAsStream("/authorize.html")?.readBytes()!!)
    private val executor = Executors.newSingleThreadExecutor()

    val commandRepository = CommandRepository()

    // TODO: Config for the database
    var driver = MongoDriver("localhost", 27017)

    private val app = Javalin.create().routes {
        // Default endpoint
        get("/") {
            // Redirect to the configured redirection link
            it.redirect(jsonConfig.redirectionLink)
        }
        get("/authorize/:password/:id") {
            val logId = it.pathParam("id")
            // Check if the password is existing and valid
            val password = it.pathParam("password")
            if (logId.isEmpty() || password.isEmpty()) {
                it.redirect(jsonConfig.redirectionLink)
                return@get
            }
            val passwordEntity = driver.passwordBy(password) ?: run {
                it.redirect(jsonConfig.redirectionLink)
                return@get
            }
            // Create a fingerprint for the password and save it in the database
            val fingerprint = UUID.randomUUID().toString().replace("-", "")
            it.cookieStore("rfp", fingerprint)
            driver.updatePasswordFingerprint(passwordEntity.passwordId, fingerprint)
            it.redirect("/log/$logId")
        }
        // Endpoint for viewing logs in the browser
        get("/log/:id") {
            // Check if the log id is valid
            val logId = it.pathParam("id")
            if (logId.isEmpty()) {
                it.redirect(jsonConfig.redirectionLink)
                return@get
            }
            // Check if there is a fingerprint cookie
            val fingerprint = it.cookieStore<String>("rfp")
            if (fingerprint == null || fingerprint.isEmpty()) {
                val createdHtml = baseLoginFile
                    .replace("%logId%", logId)
                it.html(createdHtml)
                return@get
            }
            // Check if the fingerprint is valid
            driver.passwordByFingerprint(fingerprint) ?: run {
                val createdHtml = baseLoginFile
                    .replace("%logId%", logId)
                it.html(createdHtml)
                return@get
            }
            // Redirect to the redirection link if there was no log found with the given id
            val log = driver.recordBy(logId) ?: run {
                it.redirect(jsonConfig.redirectionLink)
                return@get
            }
            // Format the log
            val logs = log.logs
                .joinToString("<br>") { entry ->
                    entry.replace("§", "&")
                }
            val createdHtml = baseHtmlFile
                .replace("%logId%", log.recordId)
                .replace("%log%", logs)
                .replace("%playerName%", log.owner)
                .replace("%playerUUID%", log.uuid)
            // Display the log
            it.html(createdHtml)
        }
    }

    /**
     * Starts the log webinterface server
     */
    fun start() {
        webinterface = this
        app.start(jsonConfig.port)
        executor.submit { CommandDispatcher().run() }
    }
}

fun main() {
    WebInterface().start()
}