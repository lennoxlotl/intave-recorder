package de.lennox.ir.mongodb

import java.util.*

interface Converter<T> {
  fun convert(obj: Any): T
}

class UUIDConverter: Converter<UUID> {
  override fun convert(obj: Any): UUID {
    return UUID.fromString(obj.toString())
  }
}

class StringConverter: Converter<String> {
  override fun convert(obj: Any): String {
    return obj.toString()
  }
}