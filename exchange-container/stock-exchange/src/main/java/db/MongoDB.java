package db;

import com.mongodb.rx.client.MongoClients;
import com.mongodb.rx.client.MongoCollection;
import com.mongodb.rx.client.MongoDatabase;
import org.bson.Document;

public class MongoDB {
    private static final MongoDatabase database
        = MongoClients.create("mongodb://172.17.0.1:27017")
        .getDatabase("stockExchange");

    public static MongoCollection<Document> getCompanies() {
        MongoCollection<Document> companies = database.getCollection("companies");
        companies.drop().toBlocking().single();
        return companies;
    }
}
