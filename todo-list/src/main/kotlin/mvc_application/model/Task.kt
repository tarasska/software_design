package mvc_application.model

enum class TaskStatus {
    OPEN,
    CLOSED
}

data class Task(
    val id: Int,
    val header: String,
    val content: String,
    var status: TaskStatus = TaskStatus.OPEN
) {
    val closed get() = status == TaskStatus.CLOSED
}

