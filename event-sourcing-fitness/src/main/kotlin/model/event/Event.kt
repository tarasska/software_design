package model.event

import model.event.DbConstants.ID_KEY
import model.event.DbConstants.TIMESTAMP_KEY
import model.event.DbConstants.USER_ID_KEY
import org.bson.Document
import java.time.LocalDateTime

open class Event (
    val id: Int,
    val userId: Int,
    val creationTime: LocalDateTime
): DbModel, Comparable<Event> {
    override fun toDocument(): Document = Document()
        .append(ID_KEY, id)
        .append(USER_ID_KEY, userId)
        .append(TIMESTAMP_KEY, creationTime.format(DbFormatter.standard))

    override fun compareTo(other: Event): Int {
        val byTime = creationTime.compareTo(other.creationTime)
        return if (byTime == 0) id.compareTo(other.id) else byTime
    }
}
