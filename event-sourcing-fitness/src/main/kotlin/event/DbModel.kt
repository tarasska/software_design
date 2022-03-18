package event

import org.bson.Document
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

object DbConstants {
    const val ID_KEY = "id"
    const val USER_ID_KEY = "userId"
    const val TIMESTAMP_KEY = "timestamp"
    const val EVENT_TYPE_KEY = "eventType"
    const val END_TIME = "endTime"
}

object DbFormatter {
    val standard = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
}

interface DbModel {
    fun toDocument(): Document
}