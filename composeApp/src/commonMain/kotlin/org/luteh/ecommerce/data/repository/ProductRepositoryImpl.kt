package org.luteh.ecommerce.data.repository

import arrow.core.Either
import org.luteh.ecommerce.domain.model.ProductDetailModel
import org.luteh.ecommerce.domain.repository.ProductRepository

class ProductRepositoryImpl : ProductRepository {
    override suspend fun getProduct(id: String): Either<Exception, ProductDetailModel> {
        TODO("Not yet implemented")
    }
}