package model.report

import java.time.Duration

data class DailyReport(
    val dateToDuration: Map<String, Duration>
)