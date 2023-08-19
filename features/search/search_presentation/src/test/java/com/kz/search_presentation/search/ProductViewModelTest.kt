package com.kz.search_presentation.search

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.PagingSource
import androidx.paging.PagingState
import androidx.paging.testing.asSnapshot
import app.cash.turbine.test
import com.appmattus.kotlinfixture.kotlinFixture
import com.kz.core_testing.TestCoroutineRule
import com.kz.search_domain.model.Product
import com.kz.search_domain.usecase.ProductUseCase
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import kotlin.test.assertEquals

class ProductViewModelTest {

    @get:Rule
    val testRule = TestCoroutineRule()

    @MockK
    lateinit var productUseCase: ProductUseCase

    lateinit var viewModel: SearchViewModel

    private val fixture = kotlinFixture()

    init {
        MockKAnnotations.init(this)
    }

    @Before
    fun setup() {
    }

    @Test
    fun `test query happy case`() = runTest {
        every { productUseCase.getProducts(any()) } returns flowOf(PagingData.empty())
        viewModel = SearchViewModel(productUseCase)
        viewModel.state.test {
            val initState = awaitItem()
            assertEquals(initState.query, "")
            viewModel.onQueryChanged("sample")
            val newState = awaitItem()
            assertEquals(newState.query, "sample")
        }
    }

    @Test
    fun `test products happy case`() = runTest {
        val expectedProducts = fixture.asSequence<Product>().take(10).toList()

        every { productUseCase.getProducts("") } returns
                Pager(
                    config = PagingConfig(
                        pageSize = 10,
                        initialLoadSize = 10
                    ),
                    pagingSourceFactory = {
                        StubProductsPagingSource(expectedProducts)
                    }).flow


        every { productUseCase.getProducts(not("")) } returns
                Pager(
                    config = PagingConfig(
                        pageSize = 10,
                        initialLoadSize = 10
                    ),
                    pagingSourceFactory = {
                        StubProductsPagingSource(listOf())
                    }).flow

        viewModel = SearchViewModel(productUseCase, StandardTestDispatcher(name = "io"))

        viewModel.state.test {
            awaitItem()
            viewModel.onQueryChanged("sap")
            delay(300)
            awaitItem()
            val firstActual = viewModel.state.map { it.products }.asSnapshot().size
            assertEquals(0, firstActual)
            viewModel.onQueryChanged("")
            delay(300)
            awaitItem()
            val secondActual = viewModel.state.map { it.products }.asSnapshot().size
            assertEquals(10, secondActual)
            cancelAndIgnoreRemainingEvents()
        }
    }

}

class StubProductsPagingSource(private val expectedValue: List<Product>) :
    PagingSource<Int, Product>() {

    init {
        println("Expected size: ${expectedValue.size}")
    }

    override fun getRefreshKey(state: PagingState<Int, Product>): Int? {
        return state.anchorPosition
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Product> {
        return LoadResult.Page(expectedValue, null, null)
    }
}