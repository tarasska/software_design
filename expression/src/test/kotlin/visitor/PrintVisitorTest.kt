package visitor

import BaseTest
import org.junit.jupiter.api.Test
import tokenizer.Tokenizer
import java.io.StringWriter
import kotlin.test.assertEquals

class PrintVisitorTest : BaseTest() {
    private fun printTest(src: String, expected: String) {
        val tokenizer = Tokenizer(src)
        val sw = StringWriter()
        PrintVisitor(sw).visitAll(tokenizer.tokenize())
        assertEquals(expected, sw.toString().trim())
    }

    @Test
    fun number() = printTest("100", "NUMBER(100)")

    @Test
    fun add() = printTest("366 + 27", "NUMBER(366) NUMBER(27) ADD")

    @Test
    fun complexExpression() = printTest(
        "(1 + 2 / 3) + (0 * 3)",
        "NUMBER(1) NUMBER(2) NUMBER(3) DIV ADD NUMBER(0) NUMBER(3) MUL ADD"
    )

    @Test
    fun complexOrder() = printTest(
        "3 + 2 * 2 -    2 /2",
    "NUMBER(3) NUMBER(2) NUMBER(2) MUL ADD NUMBER(2) NUMBER(2) DIV SUB"
    )

    @Test
    fun complexOrder2() = printTest(
        "3 + 1 * 2 * (3 - 1) - 1",
        "NUMBER(3) NUMBER(1) NUMBER(2) MUL NUMBER(3) NUMBER(1) SUB MUL ADD NUMBER(1) SUB"
    )
}