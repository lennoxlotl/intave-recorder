package de.lennox.ir.mongodb

import com.mongodb.MongoClient
import com.mongodb.MongoClientOptions
import com.mongodb.MongoCredential
import com.mongodb.ServerAddress
import com.mongodb.client.MongoCollection
import com.mongodb.client.MongoDatabase
import de.lennox.ir.Driver
import de.lennox.ir.entity.RecordEntity
import de.lennox.ir.entity.RecordQuery
import de.lennox.ir.entity.RecordQueryType
import org.bson.codecs.configuration.CodecRegistries
import org.bson.codecs.pojo.PojoCodecProvider

class MongoDriver(
  host: String,
  port: Int,
  credential: MongoCredential? = null
) : Driver {
  private val mongoClient: MongoClient
  private val mongoDatabase: MongoDatabase
  private val recordCollection: MongoCollection<RecordEntity>

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
    mongoDatabase = mongoClient.getDatabase("intaverecorder").withCodecRegistry(codecRegistry)
    recordCollection = mongoDatabase.getCollection("records", RecordEntity::class.java).withCodecRegistry(codecRegistry)
  }

  override fun pushRecord(recordEntity: RecordEntity) {
    recordCollection.insertOne(recordEntity)
  }

  override fun recordBy(id: String): RecordEntity? {
    return recordCollection.find(
      RecordQuery(
        RecordQueryType.RECORD_ID,
        id
      ).findQuery()
    ).first()
  }

}