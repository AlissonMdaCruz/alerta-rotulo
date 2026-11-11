package com.amdcruz.alertarotulo.product.ui

import app.cash.turbine.test
import com.amdcruz.alertarotulo.product.data.model.Product
import com.amdcruz.alertarotulo.product.data.model.ProductLookupResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class ProductViewModelTest {

    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun lookup_whenProductMissing_exposesNotFoundState() = runTest {
        val fakeRepository = FakeProductRepository { ProductLookupResult.NotFound }
        val viewModel = ProductViewModel(productRepository = fakeRepository)

        viewModel.uiState.test {
            assertEquals(ProductUiState.Idle, awaitItem())

            viewModel.lookup("0000000000000")
            assertEquals(ProductUiState.Loading, awaitItem())
            advanceUntilIdle()

            val actual = awaitItem()
            assertTrue(actual is ProductUiState.NotFound)
            assertEquals("0000000000000", (actual as ProductUiState.NotFound).code)
        }
    }

    @Test
    fun lookup_whenNetworkFails_exposesNetworkErrorState() = runTest {
        val fakeRepository = FakeProductRepository { ProductLookupResult.NetworkError }
        val viewModel = ProductViewModel(productRepository = fakeRepository)

        viewModel.uiState.test {
            assertEquals(ProductUiState.Idle, awaitItem())

            viewModel.lookup("3017620422003")
            assertEquals(ProductUiState.Loading, awaitItem())
            advanceUntilIdle()

            val actual = awaitItem()
            assertTrue(actual is ProductUiState.NetworkError)
            assertEquals("3017620422003", (actual as ProductUiState.NetworkError).code)
        }
    }

    @Test
    fun lookup_whenProductExists_exposesSuccessState() = runTest {
        val product = Product(
            code = "3017620422003",
            name = "Nutella",
            brand = "Ferrero",
            allergens = "milk",
            ingredients = "sugar",
            nutriScore = 'E',
            consultedAtEpochMillis = 1L,
        )
        val fakeRepository = FakeProductRepository {
            ProductLookupResult.Success(product)
        }
        val viewModel = ProductViewModel(productRepository = fakeRepository)

        viewModel.uiState.test {
            assertEquals(ProductUiState.Idle, awaitItem())

            viewModel.lookup("3017620422003")
            assertEquals(ProductUiState.Loading, awaitItem())
            advanceUntilIdle()

            assertEquals(ProductUiState.Success(product), awaitItem())
        }
    }
}
