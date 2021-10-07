package mvc_application

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

/**
 * @author tarasska
 */
@SpringBootApplication
class App

fun main(args: Array<String>) {
    runApplication<App>(*args)
}