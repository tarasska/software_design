package service.report

import db.FitnessCenterDao
import model.event.DbFormatter
import model.event.VisitEvent
import model.report.DailyReport
import model.report.TotalReport
import rx.Observable
import rx.observables.GroupedObservable
import java.time.Duration
import java.time.LocalDateTime

class ReportCommand(private val dao: FitnessCenterDao) {
    data class Visit(val from: LocalDateTime, val to: LocalDateTime) {
        val visitDuration = Duration.between(from, to)
    }

    private val stats: MutableMap<Long, MutableList<Visit>> = mutableMapOf()

    private fun visitsToStat(visitGroup: GroupedObservable<Long, VisitEvent>): MutableList<Visit>  {
        val visits: MutableList<Visit> = ArrayList()
        var last: VisitEvent? = null
        visitGroup.forEach { event ->
            if (last?.type == VisitEvent.VisitType.ENTER) {
                visits.add(Visit(last!!.creationTime, event.creationTime))
            }
            last = event
        }
        return visits
    }

    fun withPrebuild(): ReportCommand {
        dao.getAllVisits().groupBy { it.userId }.forEach { visitGroup ->
            stats[visitGroup.key] = visitsToStat(visitGroup)
        }
        return this
    }

    fun updateStat(userId: Long) {
        val maxTimeInStat = if (!stats.containsKey(userId)) {
            stats[userId] = ArrayList()
            LocalDateTime.MIN
        } else {
            stats[userId]!!.last().from
        }
        var last: VisitEvent? = null
        dao.getVisitsByUserId(userId)
            .filter { event ->
                maxTimeInStat.isBefore(event.creationTime) || maxTimeInStat.isEqual(event.creationTime)
            }
            .forEach { event ->
                if (last?.type == VisitEvent.VisitType.ENTER) {
                    stats[userId]!!.add(Visit(last!!.creationTime, event.creationTime))
                }
                last = event
            }
    }

    fun getDailyInfo(userId: Long): Observable<DailyReport> {
        val dailyStat: MutableMap<String, Duration> = mutableMapOf()
        for (visit in stats[userId] ?: ArrayList()) {
            val date = visit.from.format(DbFormatter.date)
            dailyStat.compute(date) { _, oldDur ->
                val base = oldDur ?: Duration.ZERO
                base.plus(visit.visitDuration)
            }
        }
        return Observable.just(DailyReport(dailyStat))
    }

    fun getTotalReport(userId: Long): Observable<TotalReport> {
        val visits = stats[userId] ?: ArrayList()
        return Observable.just(TotalReport(
            visits.size,
            if (visits.isEmpty()) 0
            else (Duration.between(visits.first().from, visits.last().from).toDays() + 1).toInt(),
            Duration.ofMillis(visits.map { it.visitDuration.toMillis() }.sum())
        ))
    }
}