package db

import com.mongodb.rx.client.Success
import event.SubscriptionEvent
import event.VisitEvent
import rx.Observable

interface FitnessCenter {
    fun getSubscriptionsByUserId(userId: Int): Observable<SubscriptionEvent>
    fun getActualSubscriptionByUserId(userId: Int): Observable<SubscriptionEvent>
    fun getVisitsByUserId(userId: Int): Observable<VisitEvent>

    fun addSubscription(subscription: SubscriptionEvent): Observable<Success>
    fun registerVisit(visit: VisitEvent): Observable<Success>
}