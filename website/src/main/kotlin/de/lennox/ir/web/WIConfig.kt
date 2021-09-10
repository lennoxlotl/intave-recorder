package de.lennox.ir.web

import com.google.gson.annotations.SerializedName

data class WIConfig(
  @SerializedName("port")
  val port: Int,
  @SerializedName("default_redirection_link")
  val redirectionLink: String
)
