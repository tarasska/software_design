package service.manager

import service.ServerLauncher

fun main(args: Array<String>) {
    ServerLauncher(ManagerController(), 8080)
}