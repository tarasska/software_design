package search

data class SearchResultElement(val url: String, val title: String)

class SearchResult(
    private val engine: SearchEngine,
    private val resultElements: List<SearchResultElement>
) {
    fun isFailed(): Boolean {
        return resultElements.isEmpty()
    }

    override fun toString(): String {
        return """
            Results for ${engine.name} engine:
            ${resultElements.joinToString("\n")}
        """.trimIndent()
    }
}

fun buildFailedResult(engine: SearchEngine) = SearchResult(engine, emptyList())