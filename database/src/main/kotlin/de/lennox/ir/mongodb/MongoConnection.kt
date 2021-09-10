package de.lennox.ir.mongodb

import com.mongodb.MongoClient
import com.mongodb.MongoClientOptions
import com.mongodb.MongoCredential
import com.mongodb.ServerAddress
import org.bson.codecs.configuration.CodecRegistries
import org.bson.codecs.pojo.PojoCodecProvider

class MongoConnection(
  host: String,
  port: Int,
  credential: MongoCredential?
) {
  private val mongoClient: MongoClient

  init {
    val codecRegistry = CodecRegistries.fromRegistries(
      MongoClient.getDefaultCodecRegistry(),
      CodecRegistries.fromProviders(
        PojoCodecProvider
          .builder()
          .automatic(true)
          .build()
      )
    )
    val clientOptions = MongoClientOptions
      .builder()
      .codecRegistry(codecRegistry)
      .build()
    val serverAddress = ServerAddress(host, port)
    mongoClient = if (credential != null) {
      MongoClient(
        serverAddress,
        credential,
        clientOptions
      )
    } else {
      MongoClient(
        serverAddress,
        clientOptions
      )
    }
  }

}