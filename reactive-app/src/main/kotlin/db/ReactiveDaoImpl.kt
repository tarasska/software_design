package db

import model.Product
import model.User
import rx.Observable

class ReactiveDaoImpl: WebCatalogDao {
    override fun addUser(user: User): Observable<Boolean> {
        TODO("Not yet implemented")
    }

    override fun addProduct(product: Product): Observable<Boolean> {
        TODO("Not yet implemented")
    }

    override fun getProductsByUserId(userId: Int): Observable<Product> {
        TODO("Not yet implemented")
    }
}