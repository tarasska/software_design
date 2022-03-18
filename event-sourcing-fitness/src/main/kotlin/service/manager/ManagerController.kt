package service.manager

import db.FitnessCenterDao
import model.command.SubscriptionCommand
import model.event.DbConstants
import rx.Observable
import service.AbstractController

class ManagerController(dao: FitnessCenterDao) : AbstractController() {

    private val subscriptionCommands = SubscriptionCommand(dao)

    override fun handle(endpoint: String, params: Map<String, List<String>>): Observable<String> {
        val userId = extractLongParam(DbConstants.USER_ID_KEY, params)
        return when (endpoint) {
            "create" -> {
                val endTime = extractTimeParam(DbConstants.TIMESTAMP_KEY, params)
                mapToStr(subscriptionCommands.createSubscription(userId, endTime))
            }
            "renew" -> {
                val endTime = extractTimeParam(DbConstants.TIMESTAMP_KEY, params)
                mapToStr(subscriptionCommands.renewSubscription(userId, endTime))
            }
            "info" -> mapToStr(subscriptionCommands.getInfo(userId))
            else -> Observable.just("Unexpected request to $endpoint")
        }
    }
}