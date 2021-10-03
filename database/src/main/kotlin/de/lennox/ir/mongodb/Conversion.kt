package de.lennox.ir.mongodb

interface Converter<T> {
    /**
     * Converts an object into a given generic type
     *
     * @return Converted type
     */
    fun convert(obj: Any): T
}

class StringConverter : Converter<String> {
    override fun convert(obj: Any): String {
        return obj.toString()
    }
}