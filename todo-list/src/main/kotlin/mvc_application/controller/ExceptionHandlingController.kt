package mvc_application.controller

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.ExceptionHandler
import javax.servlet.http.HttpServletRequest

@Controller
class ExceptionHandlingController {
    @ExceptionHandler(Exception::class)
    fun handleError(req: HttpServletRequest, ex: Exception): String {
        return "error"
    }
}