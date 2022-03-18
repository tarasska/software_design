package db

import com.mongodb.rx.client.MongoClients
import com.mongodb.rx.client.MongoCollection
import org.bson.Document

object MongoDB {
    private val database = MongoClients
        .create("mongodb://localhost:27017")
        .getDatabase("fitness-center")

    fun dropDatabaseBlocking() {
        database.drop().toBlocking().single()
    }

    fun getSubscriptions(): MongoCollection<Document> {
        return database.getCollection("subscriptions")
    }

    fun getVisits(): MongoCollection<Document> {
        return database.getCollection("visits")
    }

}