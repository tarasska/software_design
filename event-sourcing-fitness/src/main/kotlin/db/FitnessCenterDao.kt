package db

import com.mongodb.client.model.Filters
import com.mongodb.rx.client.Success
import event.DbConstants
import event.SubscriptionEvent
import event.VisitEvent
import rx.Observable

class FitnessCenterDao : FitnessCenter {
    private val subscriptions = MongoDB.getSubscriptions()
    private val visits = MongoDB.getVisits()

    private fun subByUserId(userId: Int): Observable<SubscriptionEvent> {
        return subscriptions
            .find(Filters.eq(DbConstants.USER_ID_KEY, userId))
            .toObservable()
            .map { doc -> SubscriptionEvent(doc) }
    }

    private fun visitByUserId(userId: Int): Observable<VisitEvent> {
        return visits
            .find(Filters.eq(DbConstants.USER_ID_KEY, userId))
            .toObservable()
            .map { doc -> VisitEvent(doc) }
    }

    override fun getSubscriptionsByUserId(userId: Int): Observable<SubscriptionEvent> {
        return subByUserId(userId).sorted()
    }

    override fun getActualSubscriptionByUserId(userId: Int): Observable<SubscriptionEvent> {
        return getSubscriptionsByUserId(userId).last()
    }

    override fun getVisitsByUserId(userId: Int): Observable<VisitEvent> {
        return visitByUserId(userId).sorted()
    }

    override fun addSubscription(subscription: SubscriptionEvent): Observable<Success> {
        return subscriptions.insertOne(subscription.toDocument())
    }

    override fun registerVisit(visit: VisitEvent): Observable<Success> {
        return visits.insertOne(visit.toDocument())
    }
}