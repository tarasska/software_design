package mvc_application.model

class TaskList(
    private val name: String
) {
    private val tasks: MutableList<Task> = ArrayList()

    constructor(name: String, src: Collection<Task>): this(name) {
        tasks.addAll(tasks)
    }
}