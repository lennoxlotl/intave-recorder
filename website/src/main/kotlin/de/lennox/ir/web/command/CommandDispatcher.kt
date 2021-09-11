package de.lennox.ir.web.command

import de.lennox.ir.web.ANSI_RED
import de.lennox.ir.web.ANSI_RESET
import de.lennox.ir.web.webinterface

private const val PRE_FORMAT = "> "

class CommandDispatcher : Runnable {
  override fun run() {
    // Get all commands
    val commands = webinterface.commandRepository.repository
    // Print the marker
    print(PRE_FORMAT)
    while (true) run loop@{
      // Check if there was a console input
      val line = readLine()
      if (line != null) {
        // Split the command input to receive valid arguments
        val args = line.split(" ").toTypedArray()
        // Check if there is a valid command
        val command = commands[args[0]] ?: run {
          println("${ANSI_RED}Invalid command! Type 'help' for all commands.$ANSI_RESET")
          print(PRE_FORMAT)
          return@loop
        }
        // Dispatch the command
        command.prepareDispatch(args.copyOfRange(1, args.size))
        // Print the marker
        print(PRE_FORMAT)
      }
    }
  }
}