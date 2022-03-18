package service.manager

import db.FitnessCenterDao
import service.ServerLauncher

fun main(args: Array<String>) {
    ServerLauncher(ManagerController(FitnessCenterDao()), 8080)
}