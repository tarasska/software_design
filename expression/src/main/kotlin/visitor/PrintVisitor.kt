package visitor

import tokenizer.*
import java.io.Writer

class PrintVisitor(private val output: Writer) : TokenVisitor<Unit> {
    override fun visit(token: NumberToken) = output.write(token.show())
    override fun visit(token: BraceToken) = output.write(token.show())

    override fun visit(token: OperationToken) = output.write(token.show())

    override fun visitAll(tokens: List<Token>) {
        ParserVisitor().visitAll(tokens).forEach { t ->
            t.accept(this)
            output.write(" ")
        }
    }
}