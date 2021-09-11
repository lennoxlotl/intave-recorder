package de.lennox.ir.entity

class PasswordEntity(var passwordId: String, var password: String, var linkedFingerprint: String) {
  // Unfortunately we need this due to MongoDB parsing
  constructor() : this("", "", "") {}
}