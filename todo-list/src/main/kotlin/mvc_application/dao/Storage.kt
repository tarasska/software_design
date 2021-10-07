package mvc_application.dao

import mvc_application.model.Task

interface Storage {
    fun createTaskList(name: String)
    fun deleteTaskList(name: String)

    fun addTask(listName: String, task: Task): Int
    fun removeTask(listName: String, taskId: Int)
}