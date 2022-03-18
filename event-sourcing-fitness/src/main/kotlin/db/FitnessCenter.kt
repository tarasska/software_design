package db

import com.mongodb.rx.client.Success
import model.event.SubscriptionEvent
import model.event.VisitEvent
import rx.Observable
import java.time.LocalDateTime

interface FitnessCenter {
    fun getSubscriptionsByUserId(userId: Long): Observable<SubscriptionEvent>
    fun getActualSubscriptionByUserId(userId: Long): Observable<SubscriptionEvent>
    fun getVisitsByUserId(userId: Long): Observable<VisitEvent>
    fun getLastVisitEvent(userId: Long): Observable<VisitEvent>

    fun addSubscription(
        userId: Long,
        creationTime: LocalDateTime,
        type: SubscriptionEvent.SubscriptionType,
        endTime: LocalDateTime
    ): Observable<Success>

    fun registerVisit(
        userId: Long,
        creationTime: LocalDateTime,
        type: VisitEvent.VisitType
    ): Observable<Success>
}