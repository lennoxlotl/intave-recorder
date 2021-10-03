package de.lennox.ir.mongodb.query

import org.bson.conversions.Bson

interface Query {
    /**
     * Creates a query for updating the object
     *
     * @return Created update query
     */
    fun updateQuery(): Bson

    /**
     * Creates a query for updating the object
     *
     * @return Created update query
     */
    fun findQuery(): Bson
}