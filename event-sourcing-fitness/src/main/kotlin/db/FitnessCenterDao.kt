package db

import com.mongodb.client.model.Filters
import com.mongodb.rx.client.MongoCollection
import com.mongodb.rx.client.Success
import model.event.DbConstants
import model.event.SubscriptionEvent
import model.event.VisitEvent
import org.bson.Document
import rx.Observable
import java.time.LocalDateTime

class FitnessCenterDao(
    private val subscriptions: MongoCollection<Document> = MongoDB.getSubscriptions(),
    private val visits: MongoCollection<Document> = MongoDB.getVisits()
) : FitnessCenter {

    private fun subsByUserId(userId: Long): Observable<SubscriptionEvent> {
        return subscriptions
            .find(Filters.eq(DbConstants.USER_ID_KEY, userId))
            .toObservable()
            .map { doc -> SubscriptionEvent(doc) }
            .defaultIfEmpty(null)
    }

    private fun visitsByUserId(userId: Long): Observable<VisitEvent> {
        return visits
            .find(Filters.eq(DbConstants.USER_ID_KEY, userId))
            .toObservable()
            .map { doc -> VisitEvent(doc) }
            .defaultIfEmpty(null)
    }

    override fun getSubscriptionsByUserId(userId: Long): Observable<SubscriptionEvent> {
        return subsByUserId(userId).sorted()
    }

    override fun getLastSubscriptionByUserId(userId: Long): Observable<SubscriptionEvent> {
        return getSubscriptionsByUserId(userId).lastOrDefault(null)
    }

    override fun getVisitsByUserId(userId: Long): Observable<VisitEvent> {
        return visitsByUserId(userId).sorted()
    }

    override fun getLastVisitEvent(userId: Long): Observable<VisitEvent> {
        return getVisitsByUserId(userId).lastOrDefault(null)
    }

    override fun getAllVisits(): Observable<VisitEvent> {
        return visits.find().toObservable().map { doc -> VisitEvent(doc) }.sorted()
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