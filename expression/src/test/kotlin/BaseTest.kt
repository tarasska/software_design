import tokenizer.ShowableToken
import tokenizer.Token
import java.util.stream.Collectors

open class BaseTest {
    protected fun showTokens(tokens: List<Token>): String {
        return tokens.stream()
            .filter {t -> t is ShowableToken }
            .map {t -> (t as ShowableToken).show()}
            .collect(Collectors.joining(" "))
    }
}