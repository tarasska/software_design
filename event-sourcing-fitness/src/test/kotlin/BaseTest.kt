import com.mongodb.rx.client.MongoClients
import db.FitnessCenterDao
import model.event.DbFormatter
import org.junit.jupiter.api.BeforeEach
import rx.Observable
import java.time.LocalDateTime

open class BaseTest {
    private val database = MongoClients
        .create("mongodb://localhost:27017")
        .getDatabase("test-db")

    protected val subscriptions = database.getCollection("subscriptions")
    protected val visits = database.getCollection("visits")
    protected val fitnessCenterDao = FitnessCenterDao(subscriptions, visits)

    protected val SUCCESS = "SUCCESS"
    @BeforeEach
    fun dropTables() {
        subscriptions.drop().toBlocking().single()
        visits.drop().toBlocking().single()
    }

    protected fun makeParams(vararg keyValues: Pair<String, Any>): Map<String, List<String>> {
        return keyValues.map {
            it.first to listOf(it.second.toString())
        }.toMap()
    }

    protected fun reqTime(time: LocalDateTime): String {
        return time.format(DbFormatter.short)
    }

    protected fun <T> getValue(observable: Observable<T>): T {
        return observable.toBlocking().single()
    }
}