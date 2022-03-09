package model

import org.bson.Document

data class Product(
    val id: Int,
    val name: String,
    var price: Double,
    var currency: Currency
) : DBEntity {
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

    override fun toDocument(): Document {
        return Document(mapOf(
            "id" to id,
            "name" to name,
            "price" to price,
            "currency" to currency.name
        ))
    }

    fun withNewCurrency(newCurrency: Currency): Product {
        this.price = Currency.convert(currency, newCurrency, price)
        this.currency = newCurrency
        return this
    }
}