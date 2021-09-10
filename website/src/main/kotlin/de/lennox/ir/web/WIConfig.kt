package de.lennox.ir.web

import com.google.gson.annotations.SerializedName

data class WIConfig(
  @SerializedName("default_redirection_link")
  val redirectionLink: String
)
