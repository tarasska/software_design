package model

import org.bson.Document

data class User(val id: Int, val name: String, val currency: Currency) {
    companion object {
        fun from(doc: Document): User {
            return User(
                doc.getInteger("id"),
                doc.getString("name"),
                Currency.valueOf(doc.getString("currency"))
            )
        }
    }
}