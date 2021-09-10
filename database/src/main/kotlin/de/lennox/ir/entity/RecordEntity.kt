package de.lennox.ir.entity

class RecordEntity(var recordId: String, var logs: List<String>, var uuid: String, var owner: String) {
  // Unfortunately we need this due to MongoDB parsing
  constructor() : this("", mutableListOf(), "", "") {}
}