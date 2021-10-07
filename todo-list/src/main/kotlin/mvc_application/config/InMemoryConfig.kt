package mvc_application.config

import mvc_application.dao.InMemoryStorage
import mvc_application.dao.Storage
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class InMemoryConfig {
    @Bean
    fun taskListStorage(): Storage {
        return InMemoryStorage()
    }
}