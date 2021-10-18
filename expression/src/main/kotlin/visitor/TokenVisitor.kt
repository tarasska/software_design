package visitor

import tokenizer.BraceToken
import tokenizer.NumberToken
import tokenizer.OperationToken

interface TokenVisitor {
    fun visit(token: NumberToken)
    fun visit(token: BraceToken)
    fun visit(token: OperationToken)
}