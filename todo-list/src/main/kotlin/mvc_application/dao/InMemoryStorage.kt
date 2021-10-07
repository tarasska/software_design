package mvc_application.dao

import mvc_application.model.Task
import mvc_application.model.TaskList
import java.util.concurrent.ConcurrentHashMap

class InMemoryStorage : Storage {
    private val nameToLists: MutableMap<String, TaskList> = ConcurrentHashMap()
    private val idToListNames: MutableMap<Int, String> = ConcurrentHashMap()
    override fun createTaskList(name: String) {
        nameToLists.putIfAbsent(name, TaskList(name))
    }

    override fun deleteTaskList(name: String) {
        TODO("Not yet implemented")
    }

    override fun addTask(listName: String, task: Task): Int {
        TODO("Not yet implemented")
    }

    override fun removeTask(listName: String, taskId: Int) {
        TODO("Not yet implemented")
    }
}