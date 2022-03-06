package db

import model.Product
import model.User
import rx.Observable

interface WebCatalogDao {
    fun addUser(user: User): Observable<Boolean>
    fun addProduct(product: Product): Observable<Boolean>

    fun getProductsByUserId(userId: Int): Observable<Product>
}