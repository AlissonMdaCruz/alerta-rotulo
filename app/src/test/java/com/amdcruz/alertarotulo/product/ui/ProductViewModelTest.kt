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
        val fakeProductRepository = FakeProductRepository { ProductLookupResult.NotFound }
        val fakeHistoryRepository = FakeHistoryRepository()
        val viewModel = ProductViewModel(
            productRepository = fakeProductRepository,
            historyRepository = fakeHistoryRepository,
        )

        viewModel.uiState.test {
            assertEquals(ProductUiState.Idle, awaitItem())

            viewModel.lookup("0000000000000")
            assertEquals(ProductUiState.Loading, awaitItem())
            advanceUntilIdle()

            val actual = awaitItem()
            assertTrue(actual is ProductUiState.NotFound)
            assertEquals("0000000000000", (actual as ProductUiState.NotFound).code)
            assertTrue(fakeHistoryRepository.savedProducts.isEmpty())
        }
    }

    @Test
    fun lookup_whenNetworkFails_exposesNetworkErrorState() = runTest {
        val fakeProductRepository = FakeProductRepository { ProductLookupResult.NetworkError }
        val fakeHistoryRepository = FakeHistoryRepository()
        val viewModel = ProductViewModel(
            productRepository = fakeProductRepository,
            historyRepository = fakeHistoryRepository,
        )

        viewModel.uiState.test {
            assertEquals(ProductUiState.Idle, awaitItem())

            viewModel.lookup("3017620422003")
            assertEquals(ProductUiState.Loading, awaitItem())
            advanceUntilIdle()

            val actual = awaitItem()
            assertTrue(actual is ProductUiState.NetworkError)
            assertEquals("3017620422003", (actual as ProductUiState.NetworkError).code)
            assertTrue(fakeHistoryRepository.savedProducts.isEmpty())
        }
    }

    @Test
    fun lookup_whenProductExists_exposesSuccessAndSavesHistory() = runTest {
        val product = Product(
            code = "3017620422003",
            name = "Nutella",
            brand = "Ferrero",
            allergens = "milk",
            ingredients = "sugar",
            nutriScore = 'E',
            consultedAtEpochMillis = 1L,
        )
        val fakeProductRepository = FakeProductRepository {
            ProductLookupResult.Success(product)
        }
        val fakeHistoryRepository = FakeHistoryRepository()
        val viewModel = ProductViewModel(
            productRepository = fakeProductRepository,
            historyRepository = fakeHistoryRepository,
        )

        viewModel.uiState.test {
            assertEquals(ProductUiState.Idle, awaitItem())

            viewModel.lookup("3017620422003")
            assertEquals(ProductUiState.Loading, awaitItem())
            advanceUntilIdle()

            assertEquals(ProductUiState.Success(product), awaitItem())
            assertEquals(listOf(product), fakeHistoryRepository.savedProducts)
        }
    }

    @Test
    fun openSnapshot_whenHistoryHasItem_exposesSuccessWithoutNetworkSave() = runTest {
        val product = Product(
            code = "3017620422003",
            name = "Nutella",
            brand = "Ferrero",
            allergens = "milk",
            ingredients = "sugar",
            nutriScore = 'E',
            consultedAtEpochMillis = 1L,
        )
        val fakeHistoryRepository = FakeHistoryRepository().also {
            it.saveOrUpdate(product)
            it.savedProducts.clear()
        }
        val fakeProductRepository = FakeProductRepository {
            error("Network lookup must not be called for history reopen")
        }
        val viewModel = ProductViewModel(
            productRepository = fakeProductRepository,
            historyRepository = fakeHistoryRepository,
        )

        viewModel.uiState.test {
            assertEquals(ProductUiState.Idle, awaitItem())

            viewModel.openSnapshot("3017620422003")
            advanceUntilIdle()

            assertEquals(ProductUiState.Success(product), awaitItem())
            assertTrue(fakeHistoryRepository.savedProducts.isEmpty())
        }
    }
}
