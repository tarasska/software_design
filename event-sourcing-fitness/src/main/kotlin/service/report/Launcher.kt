package service.report

import service.ServerLauncher

fun main(args: Array<String>) {
    ServerLauncher(ReportController(), 8081)
}