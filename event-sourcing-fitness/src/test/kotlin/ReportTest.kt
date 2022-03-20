import model.event.DbConstants
import model.event.DbFormatter
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow
import rx.Observable
import service.manager.ManagerController
import service.report.ReportController
import service.turnstile.VisitCommand
import java.time.LocalDateTime
import java.time.LocalDateTime.now
import kotlin.test.assertEquals

class ReportTest: BaseTest() {
    private val managerController = ManagerController(fitnessCenterDao)
    private val visitCommand = VisitCommand(fitnessCenterDao)

    private fun <T> mustBeSuccess(res: Observable<T>) {
        assertEquals(SUCCESS, getValue(res.map { it.toString() }))
    }

    private fun createUserById(userId: Long) {
        mustBeSuccess(managerController.handle("create", makeParams(
            DbConstants.USER_ID_KEY to userId,
            DbConstants.TIMESTAMP_KEY to reqTime(now().plusDays(31))
        )))
    }

    private fun doVisit(userId: Long, from: LocalDateTime, to: LocalDateTime) {
        mustBeSuccess(visitCommand.doEnter(userId, from))
        mustBeSuccess(visitCommand.doExit(userId, to))
    }

    @Test
    fun dailyReport() {
        assertDoesNotThrow {
            val start: LocalDateTime = LocalDateTime.parse("2022-03-23-00-00", DbFormatter.short)
            createUserById(0)
            createUserById(1)
            doVisit(0, start, start.plusMinutes(60))
            doVisit(1, start, start.plusMinutes(60))
            doVisit(0, start.plusDays(2), start.plusDays(2).plusMinutes(90))
            val reportController = ReportController(fitnessCenterDao)
            assertEquals(
                "DailyReport(dateToDuration={2022-03-23=PT1H, 2022-03-25=PT3H})",
                getValue(reportController.handle(
                    "daily_report",
                    makeParams(DbConstants.USER_ID_KEY to 0))
                ))
        }
    }

    @Test
    fun totalReport() {
        assertDoesNotThrow {
            val reportController = ReportController(fitnessCenterDao)
            val start: LocalDateTime = LocalDateTime.parse("2022-03-23-00-00", DbFormatter.short)
            createUserById(0)
            createUserById(1)
            doVisit(0, start, start.plusMinutes(60))
            doVisit(1, start, start.plusMinutes(60))
            doVisit(0, start.plusDays(2), start.plusDays(2).plusMinutes(90))
            doVisit(0, start.plusDays(10), start.plusDays(10).plusMinutes(69))
            doVisit(0, start.plusDays(14), start.plusDays(14).plusMinutes(1))
            assertEquals(
                "TotalReport(visitCount=4, totalDays=15, summaryDuration=PT3H40M)",
                getValue(reportController.handle(
                    "total_report",
                    makeParams(DbConstants.USER_ID_KEY to 0))
                ))
        }
    }
}