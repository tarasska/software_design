package model.event

import model.event.DbConstants.ID_KEY
import model.event.DbConstants.USER_ID_KEY
import org.bson.Document
import java.time.LocalDateTime

class VisitEvent(
    id: Int,
    userId: Int,
    creationTime: LocalDateTime,
    val type: VisitType
): Event(id, userId, creationTime) {
    enum class VisitType {
        ENTER,
        EXIT
    }

    constructor(doc: Document) : this(
        doc.getInteger(ID_KEY),
        doc.getInteger(USER_ID_KEY),
        LocalDateTime.parse(doc.getString(DbConstants.TIMESTAMP_KEY), DbFormatter.standard),
        VisitType.valueOf(doc.getString(DbConstants.EVENT_TYPE_KEY))
    )

    override fun toDocument(): Document
        = super.toDocument()
        .append(DbConstants.EVENT_TYPE_KEY, type.toString())
}