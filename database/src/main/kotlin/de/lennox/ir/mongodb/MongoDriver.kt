package de.lennox.ir.mongodb

import com.mongodb.MongoClient
import com.mongodb.MongoClientOptions
import com.mongodb.MongoCredential
import com.mongodb.ServerAddress
import com.mongodb.client.MongoCollection
import com.mongodb.client.MongoDatabase
import de.lennox.ir.Driver
import de.lennox.ir.entity.*
import de.lennox.ir.mongodb.query.PasswordQuery
import de.lennox.ir.mongodb.query.PasswordQueryType
import de.lennox.ir.mongodb.query.RecordQuery
import de.lennox.ir.mongodb.query.RecordQueryType
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
  private val passwordCollection: MongoCollection<PasswordEntity>

  /**
   * Initializes the Mongo Database driver
   * including the collections
   *
   * @param host The hostname of the database
   * @param port The port of the database
   * @param credential The login data of the database (optional)
   */
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
    passwordCollection = mongoDatabase.getCollection("passwords", PasswordEntity::class.java).withCodecRegistry(codecRegistry)
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

  override fun pushPassword(passwordEntity: PasswordEntity) {
    passwordCollection.insertOne(passwordEntity)
  }

  override fun passwordByPassword(id: String): PasswordEntity? {
    return passwordCollection.find(
      PasswordQuery(
        PasswordQueryType.PASSWORD_ID,
        id
      ).findQuery()
    ).first()
  }

  override fun passwordByFingerprint(id: String): PasswordEntity? {
    return passwordCollection.find(
      PasswordQuery(
        PasswordQueryType.LINKED_FINGERPRINT,
        id
      ).findQuery()
    ).first()
  }

  override fun passwordBy(password: String): PasswordEntity? {
    return passwordCollection.find(
      PasswordQuery(
        PasswordQueryType.PASSWORD,
        password
      ).findQuery()
    ).first()
  }

  override fun deletePasswordBy(id: String): Boolean {
    return passwordCollection.deleteOne(
      PasswordQuery(
        PasswordQueryType.PASSWORD_ID,
        id
      ).findQuery()
    ).wasAcknowledged()
  }

  override fun updatePasswordFingerprint(id: String, fingerprint: String) {
    passwordCollection.updateOne(
      PasswordQuery(
        PasswordQueryType.PASSWORD_ID,
        id
      ).findQuery(),
      PasswordQuery(
        PasswordQueryType.LINKED_FINGERPRINT,
        fingerprint
      ).updateQuery()
    )
  }
}