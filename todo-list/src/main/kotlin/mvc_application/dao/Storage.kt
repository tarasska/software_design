package mvc_application.dao

import mvc_application.model.TaskList

interface Storage {
    fun createTaskList(name: String)
    fun deleteTaskList(name: String)

    fun getAllLists(): List<TaskList>
    fun findListByName(name: String): TaskList?

    fun addTask(listName: String, header: String, content: String): Int
    fun removeTask(listName: String, taskId: Int)
}