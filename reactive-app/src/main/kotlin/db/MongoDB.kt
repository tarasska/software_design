package db

import com.mongodb.rx.client.MongoClients
import com.mongodb.rx.client.MongoCollection
import org.bson.Document

object MongoDB {
    private val database = MongoClients
        .create("mongodb://localhost:27017")
        .getDatabase("webCatalog")

    fun getUsers(): MongoCollection<Document> {
        return database.getCollection("users")
    }

    fun getProducts(): MongoCollection<Document> {
        return database.getCollection("products")
    }
}