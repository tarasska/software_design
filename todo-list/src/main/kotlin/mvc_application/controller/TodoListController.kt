package mvc_application.controller

import mvc_application.dao.Storage
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestParam

@Controller
class TodoListController(
    private val taskListStorage: Storage
) {
    @PostMapping("/add-list")
    fun addList(@RequestParam name: String) {
        taskListStorage.createTaskList(name)
    }
}