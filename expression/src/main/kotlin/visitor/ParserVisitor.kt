package visitor

import tokenizer.*
import java.util.ArrayDeque

class ParserVisitor : TokenVisitor<List<Token>> {
    private val stack = ArrayDeque<Token>()
    private val revNotation = mutableListOf<Token>()

    private fun less(l: OperationToken, r: OperationToken): Boolean {
        return (l is ADD || l is SUB) && (r is MUL || r is DIV)
    }

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
        while (stack.isNotEmpty()
            && stack.peek() is OperationToken
            && !less(stack.peek() as OperationToken, token)
        ) {
            revNotation.add(stack.pop())
        }
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