package search.client

import search.SearchResult

interface SearchClient {
    fun requestTopRecords(request: SearchRequest): SearchResult
}