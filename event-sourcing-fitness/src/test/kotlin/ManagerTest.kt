import model.event.DbConstants
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow
import service.manager.ManagerController
import java.time.LocalDateTime.now
import kotlin.test.assertEquals

class ManagerTest: BaseTest() {
    private val managerController = ManagerController(fitnessCenterDao)

    @Test
    fun createSub() {
        assertDoesNotThrow {
            val res = managerController.handle("create", makeParams(
                DbConstants.USER_ID_KEY to 0,
                DbConstants.TIMESTAMP_KEY to reqTime(now().plusDays(31))
            ))
            assertEquals(SUCCESS, getValue(res))
        }
    }

    @Test
    fun createSubInPast() {
        assertDoesNotThrow {
            val res = managerController.handle("create", makeParams(
                DbConstants.USER_ID_KEY to 0,
                DbConstants.TIMESTAMP_KEY to reqTime(now().minusDays(1))
            ))
            assertEquals("You can't create subscription that ends in the past.", getValue(res))
        }
    }

    @Test
    fun renew() {
        assertDoesNotThrow {
            val resCreate = managerController.handle("create", makeParams(
                DbConstants.USER_ID_KEY to 0,
                DbConstants.TIMESTAMP_KEY to reqTime(now().plusDays(1))
            ))
            assertEquals(SUCCESS, getValue(resCreate))
            val resRenew = managerController.handle("renew", makeParams(
                DbConstants.USER_ID_KEY to 0,
                DbConstants.TIMESTAMP_KEY to reqTime(now().plusDays(1))
            ))
            assertEquals(SUCCESS, getValue(resRenew))
        }
    }

    @Test
    fun renewNonExistent() {
        assertDoesNotThrow {
            val res = managerController.handle("renew", makeParams(
                DbConstants.USER_ID_KEY to 0,
                DbConstants.TIMESTAMP_KEY to reqTime(now().plusDays(1))
            ))
            assertEquals("You can't renew an inactive subscription.", getValue(res))
        }
    }

    @Test
    fun subscriptionCutsAreNotAllowed() {
        assertDoesNotThrow {
            val resCreate = managerController.handle("create", makeParams(
                DbConstants.USER_ID_KEY to 0,
                DbConstants.TIMESTAMP_KEY to reqTime(now().plusDays(100))
            ))
            assertEquals(SUCCESS, getValue(resCreate))
            val resRenew = managerController.handle("renew", makeParams(
                DbConstants.USER_ID_KEY to 0,
                DbConstants.TIMESTAMP_KEY to reqTime(now().plusDays(50))
            ))
            assertEquals("You can't cut subscription time.", getValue(resRenew))
        }
    }
}