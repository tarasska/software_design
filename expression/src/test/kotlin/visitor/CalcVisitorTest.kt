package visitor

import org.junit.jupiter.api.Test
import tokenizer.Tokenizer
import kotlin.test.assertEquals

class CalcVisitorTest {
    private fun calcTest(src: String, expected: Int) = assertEquals(
        expected,
        CalcVisitor().visitAll(
            Tokenizer(src).tokenize()
        )
    )

    @Test
    fun number() = calcTest("123", 123)

    @Test
    fun add() = calcTest("1 + 10", 11)

    @Test
    fun sub() = calcTest("1 - 2", -1)

    @Test
    fun mul() = calcTest("2 * 2", 4)

    @Test
    fun div() = calcTest("5 / 2", 2)

    @Test
    fun complexExpr() = calcTest("3 + 2 * 2 -    2 /2", 6)

    @Test
    fun exprWithBraces() = calcTest(
        "(0     +  3 -2)*  (1) - 3+( 24 +  2) *   5",
        128
    )

    @Test
    fun exprFromTask() = calcTest(
        "(23+10 ) * 5   - 3 *(32 +  5 ) *(10- 4 *  5  )+8/ 2",
        1279
    )
}