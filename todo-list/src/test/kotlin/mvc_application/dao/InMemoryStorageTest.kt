package mvc_application.dao

import mvc_application.model.Task
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow
import org.junit.jupiter.api.assertThrows
import kotlin.NoSuchElementException
import kotlin.test.assertEquals
import kotlin.test.assertNull

class InMemoryStorageTest {
   private lateinit var storage: Storage

   @BeforeEach
   fun setup() {
      storage = InMemoryStorage()
   }

   @Test
   fun listCreationTest() {
      storage.createTaskList("first")
      storage.createTaskList("second")

      assertEquals("first", storage.findListByName("first")?.name)
      assertEquals("second", storage.findListByName("second")?.name)
   }

   @Test
   fun listDeletionTest() {
      val testSize = 10
      assertDoesNotThrow { storage.deleteTaskList("first") }
      for (id in 0 until testSize) {
         storage.createTaskList("Name$id")
      }
      (0 until testSize step 2).forEach {id ->
         storage.deleteTaskList("Name$id")
      }
      for (id in 0 until testSize) {
         val name = "Name$id"
         if (id % 2 != 0) {
            assertEquals(name, storage.findListByName(name)?.name)
         } else {
            assertNull(storage.findListByName(name))
         }
      }
   }

   @Test
   fun addTask() {
      val testSize = 10
      val listName = "list"
      storage.createTaskList(listName)
      for (id in 0 until testSize) {
         storage.addTask(listName, "H$id", "C$id")
      }
      assertThat((0 until testSize).map{id -> "H$id-C$id"}.toList())
          .hasSameElementsAs(
              storage.findListByName(listName)!!.getTasks().map{t -> "${t.header}-${t.content}"}
          )
   }

   @Test
   fun addTaskToNonExistingList() {
      assertThrows<NoSuchElementException> {
         storage.addTask("error", "header", "content")
      }
   }

   @Test
   fun removeTask() {
      val testSize = 10
      val listName = "list"
      storage.createTaskList(listName)
      val ids = ArrayList<Int>()
      for (id in 0 until testSize) {
         ids.add(storage.addTask(listName, "H$id", "C$id"))
      }
      (0 until testSize step 2).forEach {id ->
         storage.removeTask(listName, ids[id])
      }
      for (id in 0 until testSize) {
         if (id % 2 != 0) {
            assertEquals(
                Task(ids[id], "H$id", "C$id"),
                storage.findListByName(listName)!!.getTasks().first{t -> t.id == ids[id]}
            )
         } else {
            assertThrows<NoSuchElementException> {
               storage.findListByName(listName)!!.getTasks().first{t -> t.id == ids[id]}
            }
         }
      }
   }
}