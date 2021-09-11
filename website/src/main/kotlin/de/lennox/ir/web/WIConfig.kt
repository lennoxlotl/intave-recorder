package de.lennox.ir.web

import com.google.gson.annotations.SerializedName

data class WIConfig(
  @SerializedName("port")
  val port: Int,
  @SerializedName("default_redirection_link")
  val redirectionLink: String,
  @SerializedName("mongodb")
  val mongoDBConfig: MongoDBConfig
)

data class MongoDBConfig(
  @SerializedName("ip")
  val ip: String,
  @SerializedName("port")
  val port: Int,
  @SerializedName("requires_authentication")
  val authenticationNeeded: Boolean,
  @SerializedName("username")
  val username: String,
  @SerializedName("password")
  val password: String,
  @SerializedName("authentication_db")
  val authDb: String
)
