package de.lennox.ir.web.command

import de.lennox.ir.web.ANSI_RED
import de.lennox.ir.web.ANSI_RESET

abstract class Command {
  private val subcommands = HashMap<String, (args: Array<out String>) -> Unit>()

  abstract fun dispatch(args: Array<out String>)

  fun prepareDispatch(args: Array<out String>) {
    // Check for possible subcommands
    if (subcommands.isNotEmpty() && args.isNotEmpty()) {
      val requestedSubcommand = args[0]
      // Search for a valid subcommand
      val subcommand = subcommands[requestedSubcommand] ?: run {
        println("${ANSI_RED}This subcommand does not exist!$ANSI_RESET")
        return
      }
      // Dispatch the subcommand
      subcommand.invoke(args.copyOfRange(1, args.size))
      return
    }
    dispatch(args)
  }

  fun subcommand(
    name: String,
    func: (args: Array<out String>) -> Unit
  ) {
    subcommands[name] = func
  }

}