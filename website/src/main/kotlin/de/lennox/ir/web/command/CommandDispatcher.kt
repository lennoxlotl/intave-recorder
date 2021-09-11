package de.lennox.ir.web.command

import de.lennox.ir.web.ANSI_RED
import de.lennox.ir.web.ANSI_RESET
import de.lennox.ir.web.webinterface

private const val PRE_FORMAT = "> "

class CommandDispatcher : Runnable {
  override fun run() {
    val commands = webinterface.commandRepository.repository
    print(PRE_FORMAT)
    while (true) run loop@{
      val line = readLine()
      if (line != null) {
        val args = line.split(" ").toTypedArray()
        val command = commands[args[0]] ?: run {
          println("${ANSI_RED}Invalid command! Type 'help' for all commands.$ANSI_RESET")
          print(PRE_FORMAT)
          return@loop
        }
        command.prepareDispatch(args.copyOfRange(1, args.size))
        print(PRE_FORMAT)
      }
    }
  }
}