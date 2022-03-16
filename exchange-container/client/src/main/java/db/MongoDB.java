package db;

import com.mongodb.rx.client.MongoClients;
import com.mongodb.rx.client.MongoCollection;
import com.mongodb.rx.client.MongoDatabase;
import org.bson.Document;

public class MongoDB {
    private static final MongoDatabase database
        = MongoClients.create("mongodb://localhost:27017")
        .getDatabase("client");

    public static MongoCollection<Document> getUsersEmptyCollection() {
        MongoCollection<Document> users = database.getCollection("users");
        users.drop().toBlocking().single();
        return users;
    }
}