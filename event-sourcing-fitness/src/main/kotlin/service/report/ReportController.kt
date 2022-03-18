package service.report

import db.FitnessCenterDao
import model.event.DbConstants
import rx.Observable
import service.AbstractController

class ReportController(dao: FitnessCenterDao) : AbstractController() {

    private val reportCommand = ReportCommand(dao).withPrebuild()

    override fun handle(endpoint: String, params: Map<String, List<String>>): Observable<String> {
        val userId = extractLongParam(DbConstants.USER_ID_KEY, params)
        reportCommand.updateStat(userId)
        return when (endpoint) {
            "daily_report" -> mapToStr(reportCommand.getDailyInfo(userId))
            "total_report" -> mapToStr(reportCommand.getTotalReport(userId))
            else -> Observable.just("Unexpected request to $endpoint")
        }
    }
}