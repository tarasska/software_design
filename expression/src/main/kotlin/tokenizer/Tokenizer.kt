package tokenizer

class Tokenizer(private val src: String) {
    private var state: TokenizerState = State()

    fun tokenize(): List<Token> {
        src.forEach { ch ->
            state = state.nextChar(ch)
            if (state is FailedState) {
                error("Tokenization failed with msg: ${(state as FailedState).message}")
            }
        }
        return state.getActualTokens()
    }
}