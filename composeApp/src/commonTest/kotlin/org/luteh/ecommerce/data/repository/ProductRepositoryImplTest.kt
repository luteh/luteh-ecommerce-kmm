package org.luteh.ecommerce.data.repository

import kotlinx.coroutines.test.runTest
import org.luteh.ecommerce.domain.model.ProductDetailModel
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertTrue

class ProductRepositoryImplTest {

    private lateinit var repository: ProductRepositoryImpl

    @BeforeTest
    fun setUp() {
        repository = ProductRepositoryImpl()
    }

    @Test
    fun `getProduct should throw NotImplementedError`() = runTest {
        assertFailsWith<NotImplementedError> { repository.getProduct("1") }
    }

    @Test
    fun `getProducts should return dummy products`() = runTest {
        val result = repository.getProducts()

        assertTrue(result.isRight())
        assertEquals(ProductDetailModel.dummies, result.getOrNull())
    }
}
