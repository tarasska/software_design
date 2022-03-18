package service.report

import db.FitnessCenterDao
import service.ServerLauncher

fun main(args: Array<String>) {
    ServerLauncher(ReportController(FitnessCenterDao()), 8081)
}