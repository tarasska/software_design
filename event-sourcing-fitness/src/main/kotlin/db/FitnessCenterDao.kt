package db

import com.mongodb.client.model.Filters
import com.mongodb.rx.client.Success
import model.event.DbConstants
import model.event.SubscriptionEvent
import model.event.VisitEvent
import rx.Observable
import java.time.LocalDateTime

class FitnessCenterDao : FitnessCenter {
    private val subscriptions = MongoDB.getSubscriptions()
    private val visits = MongoDB.getVisits()

    private fun subByUserId(userId: Long): Observable<SubscriptionEvent> {
        return subscriptions
            .find(Filters.eq(DbConstants.USER_ID_KEY, userId))
            .toObservable()
            .map { doc -> SubscriptionEvent(doc) }
    }

    private fun visitByUserId(userId: Long): Observable<VisitEvent> {
        return visits
            .find(Filters.eq(DbConstants.USER_ID_KEY, userId))
            .toObservable()
            .map { doc -> VisitEvent(doc) }
    }

    override fun getSubscriptionsByUserId(userId: Long): Observable<SubscriptionEvent> {
        return subByUserId(userId).sorted()
    }

    override fun getActualSubscriptionByUserId(userId: Long): Observable<SubscriptionEvent> {
        return getSubscriptionsByUserId(userId).last()
    }

    override fun getVisitsByUserId(userId: Long): Observable<VisitEvent> {
        return visitByUserId(userId).sorted()
    }

    override fun addSubscription(
        userId: Long,
        creationTime: LocalDateTime,
        type: SubscriptionEvent.SubscriptionType,
        endTime: LocalDateTime
    ): Observable<Success> {
        return subscriptions.count().flatMap {
            val event = SubscriptionEvent(it, userId, creationTime, type, endTime)
            return@flatMap subscriptions.insertOne(event.toDocument())
        }
    }

    override fun registerVisit(
        userId: Long,
        creationTime: LocalDateTime,
        type: VisitEvent.VisitType
    ): Observable<Success> {
        return visits.count().flatMap {
            val event = VisitEvent(it, userId, creationTime, type)
            return@flatMap visits.insertOne(event.toDocument())
        }
    }
}