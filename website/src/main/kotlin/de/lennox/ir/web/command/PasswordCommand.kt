package de.lennox.ir.web.command

import de.lennox.ir.entity.PasswordEntity
import de.lennox.ir.web.*
import kotlin.random.Random

private val charPool: List<Char> = ('a'..'z') + ('A'..'Z') + ('0'..'9')

class PasswordCommand : Command() {

  init {
    subcommand("add") { args ->
      if (args.size == 1) {
        val id = args[0]
        val generatedPassword = randomPassword(24)
        val passwordEntity = PasswordEntity(
          id,
          generatedPassword,
          ""
        )
        webinterface.driver.pushPassword(passwordEntity)
        println("${ANSI_GRAY}Successfully created a password with the identifier $ANSI_GREEN$id$ANSI_GRAY. Generated Password: $ANSI_GREEN$generatedPassword$ANSI_GRAY.$ANSI_RESET")
      } else {
        println("${ANSI_RED}Syntax: password add <id>$ANSI_RESET")
      }
    }
    subcommand("remove") { args ->
      if (args.size == 1) {
        val id = args[0]
        val passwordEntity = webinterface.driver.deletePasswordBy(id)
        val response = if (passwordEntity) {
          "${ANSI_GRAY}The password was deleted successfully!$ANSI_RESET"
        } else {
          "${ANSI_GRAY}The password could not be deleted. (Is the id correct?)$ANSI_RESET"
        }
        println(response)
      } else {
        println("${ANSI_RED}Syntax: password remove <id>$ANSI_RESET")
      }
    }
    subcommand("show") { args ->
      if (args.size == 1) {
        val id = args[0]
        val passwordEntity = webinterface.driver.passwordByPassword(id) ?: run {
          println("${ANSI_RED}This password could not be found!${ANSI_GRAY}")
          return@subcommand
        }
        println("${ANSI_GRAY}Password Information (${ANSI_RED}$id${ANSI_GRAY}):")
        println("  - Password: ${passwordEntity.password}")
        println("  - Linked fingerprint: ${passwordEntity.linkedFingerprint}$ANSI_RESET")
      } else {
        println("${ANSI_RED}Syntax: password show <id>$ANSI_RESET")
      }
    }
  }

  override fun dispatch(args: Array<out String>) {
    println("${ANSI_RED}Password Command$ANSI_GRAY:")
    println("  - password add <id> (creates a password with an identifier, the identifier is used to delete it or to get information about it)")
    println("  - password remove <id> (deletes a password)")
    println("  - password show <id> (shows information about a password)")
  }

  private fun randomPassword(length: Int): String {
    return (1..length)
      .map { Random.nextInt(0, charPool.size) }
      .map(charPool::get)
      .joinToString("")
  }
}