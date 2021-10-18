package tokenizer

import visitor.TokenVisitor

sealed class Token {
    abstract fun accept(visitor: TokenVisitor)
}

sealed class ShowableToken(private val view: String): Token() {
    fun show(): String = view
}

/**
 * Operation section.
 */
sealed class OperationToken(private val view: String): ShowableToken(view) {
    override fun accept(visitor: TokenVisitor) = visitor.visit(this)
}
object ADD : OperationToken("ADD")
object SUB : OperationToken("SUB")
object MUL : OperationToken("MUL")
object DIV : OperationToken("DIV")

/**
 * Bracket section.
 */
sealed class BraceToken(private val view: String): ShowableToken(view) {
    override fun accept(visitor: TokenVisitor) = visitor.visit(this)
}
object LEFT : BraceToken("LEFT")
object RIGHT : BraceToken("RIGHT")

/**
 * Numbers section.
 */
sealed class NumberToken(private val view: Int): ShowableToken("NUMBER($view)") {
    override fun accept(visitor: TokenVisitor) = visitor.visit(this)
}
