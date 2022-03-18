package event

import org.bson.Document
import java.time.LocalDateTime

class SubscriptionEvent(
    id: Int,
    userId: Int,
    creationTime: LocalDateTime,
    val type: SubscriptionType,
    val endTime: LocalDateTime
): Event(id, userId, creationTime) {
    enum class SubscriptionType {
        CREATED,
        RENEWED
    }

    constructor(doc: Document) : this(
        doc.getInteger(DbConstants.ID_KEY),
        doc.getInteger(DbConstants.USER_ID_KEY),
        LocalDateTime.parse(doc.getString(DbConstants.TIMESTAMP_KEY), DbFormatter.standard),
        SubscriptionType.valueOf(doc.getString(DbConstants.EVENT_TYPE_KEY)),
        LocalDateTime.parse(doc.getString(DbConstants.END_TIME), DbFormatter.standard)
    )

    override fun toDocument(): Document
        = super.toDocument()
        .append(DbConstants.EVENT_TYPE_KEY, type.toString())
        .append(DbConstants.END_TIME, endTime.format(DbFormatter.standard))
}