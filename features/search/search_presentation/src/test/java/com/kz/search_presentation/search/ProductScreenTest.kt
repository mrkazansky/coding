package com.kz.search_presentation.search

import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertTextContains
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performTextReplacement
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.kz.search_domain.model.Product
import com.kz.search_domain.usecase.ProductUseCase
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.compose.LocalKoinApplication
import org.koin.compose.LocalKoinScope
import org.koin.core.annotation.KoinInternalApi
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.dsl.module
import org.koin.mp.KoinPlatformTools
import org.koin.test.KoinTest
import org.robolectric.annotation.Config

@RunWith(AndroidJUnit4::class)
@Config(
    instrumentedPackages = ["androidx.loader.content"],
    sdk = [31]
)
class ProductScreenTest : KoinTest {

    @get:Rule
    val rule = createComposeRule()

    @MockK
    lateinit var usecase: ProductUseCase

    init {
        MockKAnnotations.init(this)
    }

    @Before
    fun setup() {
        startKoin {
            modules(
                module {
                    single { usecase }
                    viewModel { SearchViewModel(get()) }
                }
            )
        }
    }

    @After
    fun tearDown() {
        stopKoin()
    }

    @OptIn(KoinInternalApi::class)
    @Test
    fun validate_products_init_screen(): Unit = runBlocking {
        every { usecase.getProducts(any()) } returns Pager(
            config = PagingConfig(
                pageSize = 10,
                initialLoadSize = 10
            ),
            pagingSourceFactory = {
                StubProductsPagingSource(
                    listOf(
                        Product(
                            1,
                            "Product expected",
                            "0.5",
                            5f,
                            "",
                            listOf()
                        )
                    )
                )
            }).flow

        rule.setContent {
            CompositionLocalProvider(
                LocalKoinScope provides KoinPlatformTools.defaultContext()
                    .get().scopeRegistry.rootScope,
                LocalKoinApplication provides KoinPlatformTools.defaultContext().get()
            ) {
                SearchScreen()
            }
        }
        rule.onNodeWithTag("txtSearch").assertIsDisplayed()
        rule.onNodeWithTag("txtSearch").performTextReplacement("")
        delay(400)
        rule.onNodeWithText("Product expected").assertIsDisplayed()
    }

    @OptIn(KoinInternalApi::class)
    @Test
    fun validate_products_screen(): Unit = runBlocking {
        every { usecase.getProducts("empty") } returns Pager(
            config = PagingConfig(
                pageSize = 10,
                initialLoadSize = 10
            ),
            pagingSourceFactory = {
                StubProductsPagingSource(
                    listOf()
                )
            }).flow

        every { usecase.getProducts(not("empty")) } returns Pager(
            config = PagingConfig(
                pageSize = 10,
                initialLoadSize = 10
            ),
            pagingSourceFactory = {
                StubProductsPagingSource(
                    listOf(
                        Product(
                            1,
                            "Product expected",
                            "0.5",
                            5f,
                            "",
                            listOf()
                        )
                    )
                )
            }).flow

        rule.setContent {
            CompositionLocalProvider(
                LocalKoinScope provides KoinPlatformTools.defaultContext()
                    .get().scopeRegistry.rootScope,
                LocalKoinApplication provides KoinPlatformTools.defaultContext().get()
            ) {
                SearchScreen()
            }
        }

        rule.onNodeWithTag("txtSearch").assertIsDisplayed()
        rule.onNodeWithTag("txtSearch").performTextReplacement("sap")
        rule.onNodeWithTag("txtSearch").assertTextContains("sap")
        delay(400)
        rule.onNodeWithText("Product expected").assertIsDisplayed()
    }

}