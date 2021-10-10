package mvc_application.dao

import mvc_application.model.Task
import mvc_application.model.TaskList
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.atomic.AtomicInteger

class InMemoryStorage : Storage {
    private val nameToLists: MutableMap<String, TaskList> = ConcurrentHashMap()
    private val nameToTaskIdGen: MutableMap<String, AtomicInteger> = ConcurrentHashMap()

    override fun createTaskList(name: String) {
        nameToLists.putIfAbsent(name, TaskList(name))
        nameToTaskIdGen[name] = AtomicInteger(0)
    }

    override fun deleteTaskList(name: String) {
        nameToLists.remove(name)
        nameToTaskIdGen.remove(name)
    }

    override fun getAllLists(): List<TaskList> {
        return nameToLists.values.toList()
    }

    override fun findListByName(name: String): TaskList? {
        return nameToLists[name]
    }

    override fun addTask(listName: String, header: String, content: String): Int {
        val list = nameToLists[listName]
        if (list === null) {
            throw NoSuchElementException("List with provided name=$listName not found.")
        }
        val id = nameToTaskIdGen[listName]!!.getAndIncrement()
        list.addTask(Task(id, header, content))
        return id
    }

    override fun removeTask(listName: String, taskId: Int) {
        nameToLists[listName]?.removeTask(taskId)
    }
}