package db

import com.mongodb.client.model.Filters
import com.mongodb.rx.client.MongoCollection
import model.Currency
import model.Product
import model.User
import org.bson.Document
import rx.Observable

class ReactiveDaoImpl: WebCatalogDao {

    private val idName = "id"

    private val users: MongoCollection<Document> = MongoDB.getUsers()
    private val products: MongoCollection<Document> = MongoDB.getProducts()


    private fun addDocumentToCollection(
        doc: Document,
        collection: MongoCollection<Document>
    ): Observable<Boolean> {
        return collection.find(Filters.eq(idName, doc.getInteger(idName))).toObservable()
            .singleOrDefault(null)
            .flatMap { foundDoc -> when (foundDoc) {
                null -> collection.insertOne(doc).asObservable().isEmpty.map { !it }
                else -> Observable.just(false)
            }}
    }

    override fun addUser(user: User): Observable<Boolean>
        = addDocumentToCollection(user.toDocument(), users)

    override fun addProduct(product: Product): Observable<Boolean>
        = addDocumentToCollection(product.toDocument(), products)

    override fun getProductsByUserId(userId: Int): Observable<Product> {
        return users.find(Filters.eq(idName, userId)).toObservable()
            .map { Currency.valueOf(it.getString("currency")) }
            .flatMap { currency -> products.find().toObservable().map {
                Product.from(it).withNewCurrency(currency)
            }}
    }
}