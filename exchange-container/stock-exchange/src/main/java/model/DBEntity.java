package model;

import org.bson.Document;

public interface DBEntity {
    Document toDocument();
}
