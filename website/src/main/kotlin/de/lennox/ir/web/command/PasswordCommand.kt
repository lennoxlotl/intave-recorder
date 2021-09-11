package de.lennox.ir.web.command

class PasswordCommand : Command() {

  init {
    subcommand("add") { args ->
      println("add executed")
    }
    subcommand("remove") { args ->
      println("remove executed")
    }
    subcommand("show") { args ->
      println("show executed")
    }
  }

  override fun dispatch(args: Array<out String>) {
    println("Dispatched password command!")
  }
}