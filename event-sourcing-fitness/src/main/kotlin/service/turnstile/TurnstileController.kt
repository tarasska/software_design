package service.turnstile

import db.FitnessCenterDao
import model.command.VisitCommand
import model.event.DbConstants
import rx.Observable
import service.AbstractController

class TurnstileController(dao: FitnessCenterDao) : AbstractController() {

    private val visitCommand = VisitCommand(dao)

    override fun handle(endpoint: String, params: Map<String, List<String>>): Observable<String> {
        val userId = extractLongParam(DbConstants.USER_ID_KEY, params)
        return when (endpoint) {
            "enter" -> mapToStr(visitCommand.doEnter(userId))
            "exit"  -> mapToStr(visitCommand.doExit(userId))
            else -> Observable.just("Unexpected request to $endpoint")
        }
    }
}