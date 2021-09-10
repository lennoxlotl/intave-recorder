package de.lennox.ir.mongodb

import com.mongodb.MongoCredential

class MongoDriver(
  host: String,
  port: Int,
  credential: MongoCredential?
) {
  private val mongoConnection = MongoConnection(host, port, credential)

}