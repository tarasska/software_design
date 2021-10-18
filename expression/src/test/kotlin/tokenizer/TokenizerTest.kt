package tokenizer

import BaseTest
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class TokenizerTest : BaseTest() {

    private fun showTest(src: String, expected: String) {
        val tokenizer = Tokenizer(src)
        assertEquals(expected, showTokens(tokenizer.tokenize()))
    }

    @Test
    fun simpleShowTest_Add() = showTest("1 + 10", "NUMBER(1) ADD NUMBER(10)")

    @Test
    fun simpleShowTest_Mul() = showTest("366 * 228", "NUMBER(366) MUL NUMBER(228)")

    @Test
    fun simpleShowTest_Brace() = showTest(
        "(1 + 2) + 12",
        "LEFT NUMBER(1) ADD NUMBER(2) RIGHT ADD NUMBER(12)"
    )

    @Test
    fun complexShowTest() = showTest(
        "(1 + 2 * 3) / 2 + (1)",
    "LEFT NUMBER(1) ADD NUMBER(2) MUL NUMBER(3) RIGHT DIV NUMBER(2) ADD LEFT NUMBER(1) RIGHT"
    )
}