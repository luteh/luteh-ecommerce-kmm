package org.luteh.ecommerce.data.repository

import arrow.core.Either
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.withContext
import org.luteh.ecommerce.domain.model.ProductDetailModel
import org.luteh.ecommerce.domain.repository.ProductRepository

class ProductRepositoryImpl : ProductRepository {
    override suspend fun getProduct(id: String): Either<Exception, ProductDetailModel> =
        withContext(Dispatchers.IO) { TODO("Not yet implemented") }

    override suspend fun getProducts(): Either<Exception, List<ProductDetailModel>> =
        withContext(Dispatchers.IO) {
            return@withContext Either.Right(ProductDetailModel.dummies)
        }
}
