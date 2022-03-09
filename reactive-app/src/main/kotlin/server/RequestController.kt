package server

import db.WebCatalogDao
import io.netty.handler.codec.http.HttpResponseStatus
import model.Currency
import model.Product
import model.User

class RequestController(private val databaseDao: WebCatalogDao) {
    private val okStatus = HttpResponseStatus.OK

    fun handle(endpoint: String, params: Map<String, List<String>>): RequestResult = when (endpoint) {
        "add-user" -> addUser(params)
        "add-product" -> addProduct(params)
        "get-products" -> getProduct(params)
        else -> RequestResult.failWith("Unexpected endpoint")
    }

    private fun addUser(params: Map<String, List<String>>): RequestResult {
        val id = extractId(params)
        val name = extractParam("name", params)
        val currency = Currency.valueOf(extractParam("currency", params))

        return RequestResult(
            okStatus,
            databaseDao.addUser(User(id, name, currency)).map { toString() }
        )
    }

    private fun addProduct(params: Map<String, List<String>>): RequestResult {
        val id = extractId(params)
        val name = extractParam("name", params)
        val price = extractParam("price", params).toDouble()
        val currency = Currency.valueOf(extractParam("currency", params))

        return RequestResult(
            okStatus,
            databaseDao.addProduct(Product(id, name, price, currency)).map { toString() }
        )
    }

    private fun getProduct(params: Map<String, List<String>>): RequestResult {
        val id = extractId(params)
        return RequestResult(
            okStatus,
            databaseDao.getProductsByUserId(id).map { toString() }
        )
    }

    private fun extractId(params: Map<String, List<String>>): Int {
        return extractParam("id", params).toInt()
    }

    private fun extractParam(name: String, params: Map<String, List<String>>): String {
        return (params[name] ?: error("Required parameter $name not found "))[0]
    }
}