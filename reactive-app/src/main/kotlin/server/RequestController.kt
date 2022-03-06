package server

import db.WebCatalogDao
import io.netty.handler.codec.http.HttpResponseStatus
import rx.Observable

class RequestController(private val db: WebCatalogDao) {

    fun handle(endpoint: String, params: Map<String, List<String>>): RequestResult = when (endpoint) {
        "add-user" -> addUser(params)
        "add-product" -> addProduct(params)
        "get-products" -> getProduct(params)
        else -> RequestResult.failWith("Unexpected endpoint")
    }

    private fun addUser(params: Map<String, List<String>>): RequestResult {
        return RequestResult.failWith("No impl")
    }

    private fun addProduct(params: Map<String, List<String>>): RequestResult {
        return RequestResult.failWith("No impl")
    }

    private fun getProduct(params: Map<String, List<String>>): RequestResult {
        return RequestResult.failWith("No impl")
    }
}