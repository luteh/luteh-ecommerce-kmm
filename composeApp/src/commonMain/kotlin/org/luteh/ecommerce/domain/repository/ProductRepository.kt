package org.luteh.ecommerce.domain.repository

import arrow.core.Either
import org.luteh.ecommerce.domain.model.ProductDetailModel

interface ProductRepository {
    suspend fun getProduct(id: String): Either<Exception, ProductDetailModel>
}