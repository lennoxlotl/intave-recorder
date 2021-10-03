package de.lennox.ir

import de.lennox.ir.entity.PasswordEntity
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

    /**
     * Pushes a Password into the selected database type
     *
     * @param passwordEntity The entity being added to the database
     */
    fun pushPassword(passwordEntity: PasswordEntity)

    /**
     * Fetches a Password by its id from the database
     *
     * @param id The requested record id
     * @return Password entity found in the database or null
     */
    fun passwordByPassword(id: String): PasswordEntity?

    /**
     * Fetches a password by a browser fingerprint
     *
     * @param id The browser fingerprint
     * @return Password entity found in the database or null
     */
    fun passwordByFingerprint(id: String): PasswordEntity?

    /**
     * Fetches a password by the password attatched to it
     *
     * @param password The password
     * @return Password entity found in the database or null
     */
    fun passwordBy(password: String): PasswordEntity?

    /**
     * Deletes a password by its id
     *
     * @param id The password id
     * @return The success state of the deletion
     */
    fun deletePasswordBy(id: String): Boolean

    /**
     * Updates a passwords linked fingerprint
     *
     * @param id The password id
     * @param fingerprint The fingerprint which will be linked
     */
    fun updatePasswordFingerprint(id: String, fingerprint: String)
}