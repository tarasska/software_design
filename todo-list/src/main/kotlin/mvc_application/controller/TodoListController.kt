package mvc_application.controller

import mvc_application.dao.Storage
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestParam

@Controller
class TodoListController(
    private val taskListStorage: Storage
) {
    @PostMapping("/add-list")
    fun addList(@RequestParam name: String): String {
        taskListStorage.createTaskList(name)
        return "redirect:/get-lists"
    }

    @GetMapping("/get-lists")
    fun getLists(map: Model): String {
        map.addAttribute("taskLists", taskListStorage.getAllLists())
        return "index"
    }

    @GetMapping("/get-list")
    fun getList(@RequestParam listName: String, map: Model): String {
        map.addAttribute("taskList", taskListStorage.findListByName(listName))
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
        map.addAttribute("taskList", taskListStorage.findListByName(taskListName))
        return "tasklist"
    }
}