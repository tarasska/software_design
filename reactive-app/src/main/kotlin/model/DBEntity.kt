package model

import org.bson.Document

interface DBEntity {
    fun toDocument(): Document
}