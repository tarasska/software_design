package mvc_application.controller

import mvc_application.dao.Storage
import mvc_application.model.TaskStatus
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestParam

@Controller
class TodoListController(
    private val taskListStorage: Storage
) {

    private fun updateTaskListView(map: Model, name: String) {
        map.addAttribute("taskList", taskListStorage.findListByName(name))
    }

    @PostMapping("/create-list")
    fun createList(@RequestParam name: String): String {
        taskListStorage.createTaskList(name)
        return "redirect:/get-lists"
    }

    @PostMapping("/delete-list")
    fun deleteList(@RequestParam name: String): String {
        taskListStorage.deleteTaskList(name)
        return "redirect:/get-lists"
    }

    @GetMapping("/get-lists")
    fun getLists(map: Model): String {
        map.addAttribute("taskLists", taskListStorage.getAllLists())
        return "index"
    }

    @GetMapping("/get-list")
    fun getList(@RequestParam listName: String, map: Model): String {
        updateTaskListView(map, listName)
        return "tasklist"
    }

    @PostMapping("/add-task")
    fun addTask(
        @RequestParam header: String,
        @RequestParam content: String,
        @RequestParam taskListName: String,
        map: Model
    ): String {
        taskListStorage.addTask(taskListName, header, content)
        updateTaskListView(map, taskListName)
        return "tasklist"
    }

    @PostMapping("/remove-task")
    fun removedTask(
        @RequestParam taskId: Int,
        @RequestParam taskListName: String,
        map: Model
    ): String {
        taskListStorage.removeTask(taskListName, taskId)
        updateTaskListView(map, taskListName)
        return "tasklist"
    }

    @PostMapping("/close-task")
    fun closeTask(
        @RequestParam taskId: Int,
        @RequestParam taskListName: String,
        map: Model
    ): String {
        val task = taskListStorage.findListByName(taskListName)!!.getTasks().first{t -> t.id == taskId}
        task.status = TaskStatus.CLOSED
        updateTaskListView(map, taskListName)
        return "tasklist"
    }
}