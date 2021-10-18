package visitor

import BaseTest
import org.junit.jupiter.api.Test
import tokenizer.Tokenizer
import kotlin.test.assertEquals

class ParserVisitorTest : BaseTest() {

    private fun revNotationTest(src: String, expected: String) {
        val tokenizer = Tokenizer(src)
        assertEquals(
            expected,
            showTokens(
                ParserVisitor().visitAll(tokenizer.tokenize())
            )
        )
    }

    @Test
    fun addTest() = revNotationTest("3 + 4", "NUMBER(3) NUMBER(4) ADD")

    @Test
    fun mulSubTest() = revNotationTest(
        "10 - 3 * 4",
        "NUMBER(10) NUMBER(3) NUMBER(4) MUL SUB"
    )

    @Test
    fun braceTest() = revNotationTest(
        "(10 - 3) / 4",
        "NUMBER(10) NUMBER(3) SUB NUMBER(4) DIV"
    )
}