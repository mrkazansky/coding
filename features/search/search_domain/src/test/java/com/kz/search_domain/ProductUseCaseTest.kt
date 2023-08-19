package com.kz.search_domain

import androidx.paging.PagingData
import app.cash.turbine.test
import com.appmattus.kotlinfixture.kotlinFixture
import com.kz.search_domain.model.Product
import com.kz.search_domain.repository.CartRepository
import com.kz.search_domain.repository.ProductRepository
import com.kz.search_domain.usecase.ProductUseCase
import com.kz.search_domain.usecase.impl.ProductUseCaseImpl
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import kotlin.test.assertNotEquals

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ProductUseCaseTest {

    @MockK
    lateinit var productRepository: ProductRepository

    @MockK
    lateinit var cartRepository: CartRepository

    private lateinit var useCase: ProductUseCase

    val fixture = kotlinFixture()

    init {
        MockKAnnotations.init(this)
    }

    @Before
    fun setup() {
        useCase = ProductUseCaseImpl(productRepository, cartRepository)
    }

    @Test
    fun `test getProducts happy case`() = runTest {
        val expectedPagingData = PagingData.empty<Product>()
        val query = "sample"

        coEvery { productRepository.getProducts(query) } returns flowOf(expectedPagingData)

        val actual = useCase.getProducts(query)
        actual.test {
            assertEquals(expectMostRecentItem(), expectedPagingData)
        }
    }

    @Test
    fun `test getProducts fail case`() = runTest {

        val expectedPagingData = PagingData.empty<Product>()
        val unexpectedPagingData = PagingData.from(listOf(fixture<Product>()))

        val query = "sample"

        coEvery { productRepository.getProducts(not(query)) } returns flowOf(expectedPagingData)
        coEvery { productRepository.getProducts(query) } returns flowOf(unexpectedPagingData)


        val actual = useCase.getProducts(query)
        actual.test {
            assertNotEquals(expectMostRecentItem(), expectedPagingData)
        }
    }
}