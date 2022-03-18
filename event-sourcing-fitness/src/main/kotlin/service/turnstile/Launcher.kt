package service.turnstile

import service.ServerLauncher

fun main(args: Array<String>) {
    ServerLauncher(TurnstileController(), 8082)
}