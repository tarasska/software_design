package mvc_application.model

class TaskList(
    val name: String
) {
    private val tasks: MutableList<Task> = ArrayList()

    constructor(name: String, src: Collection<Task>): this(name) {
        tasks.addAll(tasks)
    }

    fun addTask(task: Task) = tasks.add(task)

    fun removeTask(taskId: Int) = tasks.removeIf {task -> task.id == taskId}

    fun getTasks(): MutableList<Task> = tasks
}