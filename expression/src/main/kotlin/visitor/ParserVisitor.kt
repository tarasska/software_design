package visitor

import tokenizer.*
import java.util.ArrayDeque

class ParserVisitor : TokenVisitor {
    private val stack = ArrayDeque<Token>()
    private val revNotation = mutableListOf<Token>()

    override fun visit(token: NumberToken) {
        revNotation.add(token)
    }

    override fun visit(token: BraceToken) {
        when (token) {
            LEFT -> stack.push(token)
            RIGHT -> {
                while (stack.isNotEmpty() && stack.first() !is LEFT) {
                    revNotation.add(stack.pop())
                }
                if (stack.isEmpty()) {
                    error("Braces mismatched")
                } else {
                    stack.pop()
                }
            }
        }
    }

    override fun visit(token: OperationToken) {
        stack.push(token)
    }

    override fun visitAll(tokens: List<Token>): List<Token> {
        tokens.forEach { it.accept(this) }
        while (stack.isNotEmpty()) {
            val elem = stack.pop()
            if (elem !is BraceToken) {
                revNotation.add(elem)
            }
        }
        return revNotation
    }
}