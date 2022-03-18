package model.report

import java.time.Duration

data class TotalReport(
    val visitCount: Int,
    val totalDays: Int,
    val summaryDuration: Duration
)