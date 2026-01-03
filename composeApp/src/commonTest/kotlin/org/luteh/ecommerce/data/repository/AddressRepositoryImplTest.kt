package org.luteh.ecommerce.data.repository

import app.cash.turbine.test
import dev.mokkery.MockMode
import dev.mokkery.answering.returns
import dev.mokkery.every
import dev.mokkery.everySuspend
import dev.mokkery.matcher.any
import dev.mokkery.mock
import dev.mokkery.verifySuspend
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.luteh.ecommerce.data.local.dao.ShippingAddressDao
import org.luteh.ecommerce.data.local.entity.ShippingAddressEntity
import org.luteh.ecommerce.domain.model.ShippingAddress

class AddressRepositoryImplTest {

    private lateinit var repository: AddressRepositoryImpl
    private lateinit var shippingAddressDao: ShippingAddressDao

    @BeforeTest
    fun setUp() {
        shippingAddressDao = mock(MockMode.autoUnit)
        repository = AddressRepositoryImpl(shippingAddressDao)
    }

    @Test
    fun `getLastUsedAddress should return address when it exists`() = runTest {
        val entity =
            ShippingAddressEntity(
                fullName = "John Doe",
                addressLine = "123 Main St",
                city = "New York",
                postalCode = "10001",
                phoneNumber = "1234567890",
            )
        every { shippingAddressDao.getShippingAddress() } returns flowOf(entity)

        repository.getLastUsedAddress().test {
            val result = awaitItem()
            assertEquals("John Doe", result?.fullName)
            assertEquals("123 Main St", result?.addressLine)
            assertEquals("New York", result?.city)
            assertEquals("10001", result?.postalCode)
            assertEquals("1234567890", result?.phoneNumber)
            awaitComplete()
        }
    }

    @Test
    fun `getLastUsedAddress should return null when no address exists`() = runTest {
        every { shippingAddressDao.getShippingAddress() } returns flowOf(null)

        repository.getLastUsedAddress().test {
            val result = awaitItem()
            assertNull(result)
            awaitComplete()
        }
    }

    @Test
    fun `saveLastUsedAddress should insert address into dao`() = runTest {
        val address =
            ShippingAddress(
                fullName = "Jane Doe",
                addressLine = "456 Elm St",
                city = "Los Angeles",
                postalCode = "90001",
                phoneNumber = "0987654321",
            )
        everySuspend { shippingAddressDao.insertShippingAddress(any()) } returns Unit

        repository.saveLastUsedAddress(address)

        val expectedEntity =
            ShippingAddressEntity(
                fullName = "Jane Doe",
                addressLine = "456 Elm St",
                city = "Los Angeles",
                postalCode = "90001",
                phoneNumber = "0987654321",
            )
        verifySuspend { shippingAddressDao.insertShippingAddress(expectedEntity) }
    }
}
