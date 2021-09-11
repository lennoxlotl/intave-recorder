package de.lennox.ir.mongodb.query

import com.mongodb.client.model.Filters
import com.mongodb.client.model.Updates
import de.lennox.ir.mongodb.Converter
import de.lennox.ir.mongodb.StringConverter
import org.bson.conversions.Bson

class PasswordQuery(
  private val type: PasswordQueryType,
  private val obj: Any
): Query {
  override fun updateQuery(): Bson {
    return Updates.set(type.type, type.converter.convert(obj))
  }

  override fun findQuery(): Bson {
    return Filters.eq(type.type, type.converter.convert(obj))
  }
}

enum class PasswordQueryType(
  val type: String,
  val converter: Converter<*>
) {
  PASSWORD_ID("passwordId", StringConverter()),
  PASSWORD("password", StringConverter()),
  LINKED_FINGERPRINT("linkedFingerprint", StringConverter())
}