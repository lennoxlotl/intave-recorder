package de.lennox.ir

import de.lennox.ir.entity.RecordEntity

interface Driver {

  /**
   * Pushes a Record into the selected database type
   *
   * @param recordEntity The entity being added to the database
   */
  fun pushRecord(recordEntity: RecordEntity)

  /**
   * Fetches a Record by its id from the database
   *
   * @param id The requested record id
   * @return Record entity found in the database or null
   */
  fun recordBy(id: String): RecordEntity?

}