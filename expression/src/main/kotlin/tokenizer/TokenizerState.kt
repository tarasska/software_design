package tokenizer

sealed class TokenizerState {
    protected val tokens = mutableListOf<Token>()

    fun getActualTokens(): List<Token> = tokens

    abstract fun nextChar(ch: Char): TokenizerState

}

class State : TokenizerState() {

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
        in '0'..'9' -> NumberState(ch - '0')
        else -> FailedState(ch)
    }
}

class NumberState(
    private val number: Int
) : TokenizerState() {

    override fun nextChar(ch: Char): TokenizerState = when (ch) {
        in '0'..'9' -> NumberState(number * 10 + (ch - '0'))
        else -> {
            tokens.add(NumberToken(number))
            State().nextChar(ch)
        }
    }
}

class FailedState(ch: Char): TokenizerState() {

    val message = "Unable to tokenize char: $ch"

    override fun nextChar(ch: Char): TokenizerState = this
}