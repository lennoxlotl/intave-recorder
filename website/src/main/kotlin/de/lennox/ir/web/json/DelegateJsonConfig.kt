package de.lennox.ir.web.json

import java.io.File
import kotlin.reflect.KProperty

class DelegateJsonConfig<T>(
    configPath: File,
    genericTypeClass: Class<T>,
    default: T
) {
    private val jsonConfig = JsonConfig(configPath)

    private var parsedConfigObject: T = jsonConfig.loadFromClass(genericTypeClass) ?: run {
        jsonConfig.save(default)
        default
    }

    operator fun getValue(
        t: Any?,
        property: KProperty<*>
    ): T {
        // Return the parsed object
        return parsedConfigObject
    }

    operator fun setValue(
        t: Any?,
        property: KProperty<*>,
        t1: T
    ) {
        // Save the json object
        jsonConfig.save(t1)
        parsedConfigObject = t1
    }
}