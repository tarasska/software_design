package search

data class SearchResultElement(val url: String, val title: String)

class SearchResult(
    val engine: SearchEngine,
    val resultElements: List<SearchResultElement>
) {
    fun isFailed(): Boolean {
        return resultElements.isEmpty()
    }
}

fun buildFailedResult(engine: SearchEngine) = SearchResult(engine, emptyList())