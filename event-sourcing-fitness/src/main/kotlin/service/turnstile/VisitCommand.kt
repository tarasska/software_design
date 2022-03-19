package service.turnstile

import com.mongodb.rx.client.Success
import db.FitnessCenterDao
import model.event.VisitEvent
import rx.Observable
import java.time.LocalDateTime
import kotlin.IllegalStateException

class VisitCommand(private val fitnessCenterDao: FitnessCenterDao) {

    private fun checkDoubleEnter(userId: Long, now: LocalDateTime): Observable<Success> {
        return fitnessCenterDao.getLastVisitEvent(userId)
            .flatMap { visit ->
                if (visit === null || visit.type == VisitEvent.VisitType.EXIT) {
                    fitnessCenterDao.registerVisit(userId, now, VisitEvent.VisitType.ENTER)
                } else {
                    Observable.error(IllegalStateException(
                        "You have already used your card to enter."
                    ))
                }
            }
    }

    private fun checkSubscription(userId: Long, now: LocalDateTime): Observable<Success> {
        return fitnessCenterDao.getLastSubscriptionByUserId(userId)
            .flatMap {
                if (it !== null && it.endTime.isAfter(now)) {
                    checkDoubleEnter(userId, now)
                } else {
                    Observable.error(IllegalStateException(
                        "You don't have a active subscription."
                    ))
                }
            }
    }

    fun doEnter(userId: Long) : Observable<Success> {
        val now = LocalDateTime.now()
        return checkSubscription(userId, now)
    }

    fun doExit(userId: Long): Observable<Success> {
        return fitnessCenterDao.getLastVisitEvent(userId)
            .flatMap {
                if (it?.type == VisitEvent.VisitType.ENTER) {
                    Observable.just(Success.SUCCESS)
                } else {
                    Observable.error(IllegalStateException(
                        "You should enter before exit."
                    ))
                }
            }
    }
}