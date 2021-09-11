package de.lennox.ir.web.command

class CommandRepository {
  internal val repository = HashMap<String, Command>()

  init {
    repository["password"] = PasswordCommand()
  }
}