package com.kz.micro_benchmark

import androidx.benchmark.junit4.BenchmarkRule
import androidx.benchmark.junit4.measureRepeated
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.kz.search_data.di.searchDataModule
import com.kz.search_domain.di.searchDomainModule
import com.kz.search_domain.model.Product
import com.kz.search_domain.usecase.ProductUseCase
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.koin.android.ext.koin.androidContext
import org.koin.core.KoinApplication
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.dsl.koinApplication

/**
 * Benchmark, which will execute on an Android device.
 *
 * The body of [BenchmarkRule.measureRepeated] is measured in a loop, and Studio will
 * output the result. Modify your code to see how it affects performance.
 */
@RunWith(AndroidJUnit4::class)
class ExampleBenchmark {


    @get:Rule
    val benchmarkRule = BenchmarkRule()

    private lateinit var koinApplication: KoinApplication

    @Before
    fun setup() {
        koinApplication = startKoin {
            androidContext(ApplicationProvider.getApplicationContext())
            modules(listOf(searchDomainModule, searchDataModule))
        }
    }


    @After
    fun tearDown() {
        stopKoin()
    }

    @Test
    fun benchmarkGetProducts() {
        benchmarkRule.measureRepeated {
            var usecase: ProductUseCase? = null
            runWithTimingDisabled {
                usecase = koinApplication.koin.get()
            }
            usecase?.getProducts("hello")
        }
        assert(1 == 1)
    }

    @Test
    fun benchmarkSaveProduct() {
        benchmarkRule.measureRepeated {
            var usecase: ProductUseCase? = null
            runWithTimingDisabled {
                usecase = koinApplication.koin.get()
            }
            runBlocking {
                val product = Product(1,"Test", "100",4.5f, "", listOf())
                usecase?.addToCart(product)
            }
        }
        assert(1 == 1)
    }
}