package model

import org.bson.Document

data class Product(
    val id: Int,
    val name: String,
    val price: Double,
    val currency: Currency
) {
    companion object {
        fun from(doc: Document): Product {
            return Product(
                doc.getInteger("id"),
                doc.getString("name"),
                doc.getDouble("price"),
                Currency.valueOf(doc.getString("currency"))
            )
        }
    }
}