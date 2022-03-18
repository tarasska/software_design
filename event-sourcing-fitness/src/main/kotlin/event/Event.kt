package event

import event.DbConstants.ID_KEY
import event.DbConstants.TIMESTAMP_KEY
import event.DbConstants.USER_ID_KEY
import org.bson.Document
import java.time.LocalDateTime

open class Event (
    val id: Int,
    val userId: Int,
    val creationTime: LocalDateTime
): DbModel {
    override fun toDocument(): Document = Document()
        .append(ID_KEY, id)
        .append(USER_ID_KEY, userId)
        .append(TIMESTAMP_KEY, creationTime.format(DbFormatter.standard))
}
