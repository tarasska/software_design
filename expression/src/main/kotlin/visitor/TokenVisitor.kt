package visitor

import tokenizer.BraceToken
import tokenizer.NumberToken
import tokenizer.OperationToken
import tokenizer.Token

interface TokenVisitor<out R> {
    fun visit(token: NumberToken)
    fun visit(token: BraceToken)
    fun visit(token: OperationToken)
    fun visitAll(tokens: List<Token>): R
}