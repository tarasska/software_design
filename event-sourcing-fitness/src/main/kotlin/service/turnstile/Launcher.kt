package service.turnstile

import db.FitnessCenterDao
import service.ServerLauncher

fun main(args: Array<String>) {
    ServerLauncher(TurnstileController(FitnessCenterDao()), 8082)
}