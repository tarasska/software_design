package tokenizer

sealed class TokenizerState(protected val tokens: MutableList<Token>) {

    /**
     * Returns actual tokens with possibly uncompleted token (as number)
     */
    open fun getActualTokens(): List<Token> = tokens

    abstract fun nextChar(ch: Char): TokenizerState
}

class State(tokens: MutableList<Token>) : TokenizerState(tokens) {

    private fun selfState(token: Token): TokenizerState {
        tokens.add(token)
        return this
    }

    override fun nextChar(ch: Char): TokenizerState = when (ch) {
        '+' -> selfState(ADD)
        '-' -> selfState(SUB)
        '*' -> selfState(MUL)
        '/' -> selfState(DIV)
        '(' -> selfState(LEFT)
        ')' -> selfState(RIGHT)
        in '0'..'9' -> NumberState(tokens, ch - '0')
        ' ' -> this
        else -> FailedState(tokens, ch)
    }
}

class NumberState(
    tokens: MutableList<Token>,
    private val number: Int
) : TokenizerState(tokens) {

    override fun nextChar(ch: Char): TokenizerState = when (ch) {
        in '0'..'9' -> NumberState(tokens, number * 10 + (ch - '0'))
        else -> {
            tokens.add(NumberToken(number))
            State(tokens).nextChar(ch)
        }
    }

    override fun getActualTokens(): List<Token> {
        val completedTokens = tokens.toMutableList()
        completedTokens.add(NumberToken(number))
        return completedTokens
    }
}

class FailedState(tokens: MutableList<Token>, ch: Char): TokenizerState(tokens) {

    val message = "Unable to tokenize char: $ch"

    override fun nextChar(ch: Char): TokenizerState = this
}