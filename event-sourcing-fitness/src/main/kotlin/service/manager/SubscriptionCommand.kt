package service.manager

import com.mongodb.rx.client.Success
import db.FitnessCenterDao
import model.event.SubscriptionEvent
import model.event.SubscriptionEvent.SubscriptionType
import rx.Observable
import java.time.LocalDateTime

class SubscriptionCommand(private val dao: FitnessCenterDao) {
    fun createSubscription(userId: Long, endTime: LocalDateTime): Observable<Success> {
        val now = LocalDateTime.now()
        if (endTime.isBefore(now)) {
            return Observable.error(
                IllegalStateException(
                    "You can't create subscription that ends in the past."
                )
            )
        }
        return dao.getLastSubscriptionByUserId(userId)
            .flatMap {
                if (it === null || it.endTime.isBefore(now)) {
                    dao.addSubscription(userId, now, SubscriptionType.CREATED, endTime)
                } else {
                    Observable.error(IllegalStateException("You have active subscription."))
                }
            }
    }

    fun renewSubscription(userId: Long, endTime: LocalDateTime): Observable<Success> {
        val now = LocalDateTime.now()
        return dao.getLastSubscriptionByUserId(userId)
            .flatMap {
                if (it === null || it.endTime.isBefore(now)) {
                    Observable.error(IllegalStateException("You can't renew an inactive subscription."))
                } else if (it.endTime.isBefore(endTime)) {
                    Observable.error(IllegalStateException("You can't cut subscription time."))
                } else {
                    dao.addSubscription(userId, now, SubscriptionType.CREATED, endTime)
                }
            }
    }

    fun getInfo(userId: Long): Observable<SubscriptionEvent> {
        return dao.getLastSubscriptionByUserId(userId)
    }
}