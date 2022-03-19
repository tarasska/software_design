import model.event.DbConstants
import model.event.VisitEvent
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow
import service.manager.ManagerController
import service.turnstile.TurnstileController
import java.time.LocalDateTime.now
import kotlin.test.assertEquals

class TurnstileControllerTest: BaseTest() {
    private val managerController = ManagerController(fitnessCenterDao)
    private val turnstileController = TurnstileController(fitnessCenterDao)

    @Test
    fun enter() {
        assertDoesNotThrow {
            assertEquals(SUCCESS, getValue(managerController.handle("create", makeParams(
                DbConstants.USER_ID_KEY to 0,
                DbConstants.TIMESTAMP_KEY to reqTime(now().plusDays(31))
            ))))
            val enterOk = turnstileController.handle("enter", makeParams(DbConstants.USER_ID_KEY to 0))
            assertEquals(SUCCESS, getValue(enterOk))
        }
    }

    @Test
    fun enterWithoutSubscription() {
        assertDoesNotThrow {
            val enterFail = turnstileController.handle("enter", makeParams(DbConstants.USER_ID_KEY to 0))
            assertEquals("You don't have a active subscription.", getValue(enterFail))
        }
    }

    @Test
    fun exit() {
        assertDoesNotThrow {
            visits.insertOne(VisitEvent(
                0,
                0,
                now().minusMinutes(5),
                VisitEvent.VisitType.ENTER
            ).toDocument()).toBlocking().single()
            val exitOk = turnstileController.handle("exit", makeParams(DbConstants.USER_ID_KEY to 0))
            assertEquals(SUCCESS, getValue(exitOk))
        }
    }

    @Test
    fun unexpectedExit() {
        assertDoesNotThrow {
            val exitOk = turnstileController.handle("exit", makeParams(DbConstants.USER_ID_KEY to 0))
            assertEquals("You should enter before exit.", getValue(exitOk))
        }
    }
}