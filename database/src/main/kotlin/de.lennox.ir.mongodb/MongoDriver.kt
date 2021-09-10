package de.lennox.ir.mongodb

import com.mongodb.MongoCredential

class MongoDriver(
  private val host: String,
  private val port: Int,
  private val credential: MongoCredential?
) {

  private val mongoConnection = MongoConnection(host, port, credential)

}