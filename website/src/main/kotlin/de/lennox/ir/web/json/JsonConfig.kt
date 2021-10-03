package de.lennox.ir.web.json

import de.lennox.ir.web.gson
import java.io.File

open class JsonConfig(configFile: File) {

    val file = configFile

    init {
        // Create all parental directories
        runCatching {
            if (!file.parentFile.exists()) {
                this.file.parentFile.mkdirs()
            }
        }
        // Create the file if it does not exist
        if (!file.exists()) {
            this.file.createNewFile()
        }
    }

    /**
     * Saves a config spec to the attached file
     *
     * @param configSpec The saved object (config spec)
     */
    fun save(configSpec: Any?) {
        // Write down the created json text to the file
        file.writeText(gson.toJson(configSpec))
    }

    /**
     * Load the config from the file content
     *
     * @return Parsed object
     */
    inline fun <reified T> load(): T? {
        // Check if the file exists and if there is content to read
        if (file.exists() && file.readText().isNotEmpty()) {
            return gson.fromJson(
                file.readText(),
                T::class.java
            )
        }
        // No existing config was found
        return null
    }

    /**
     * Load the json object and parse it with a given class
     *
     * @return The parsed object
     */
    fun <T> loadFromClass(clazz: Class<T>): T? {
        // Check if the file exists and if there is content to read
        if (file.exists() && file.readText().isNotEmpty()) {
            return gson.fromJson(
                file.readText(),
                clazz
            )
        }
        // No existing config was found
        return null
    }

}