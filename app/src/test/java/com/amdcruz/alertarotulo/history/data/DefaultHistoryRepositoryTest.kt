package com.amdcruz.alertarotulo.history.data

import app.cash.turbine.test
import com.amdcruz.alertarotulo.product.data.model.Product
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class DefaultHistoryRepositoryTest {

    @Test
    fun saveOrUpdate_dedupesByCodeAndMovesToTop() = runTest {
        val repository = DefaultHistoryRepository(historyDao = FakeHistoryDao())
        repository.saveOrUpdate(product(code = "111", name = "First", consultedAt = 1L))
        repository.saveOrUpdate(product(code = "222", name = "Second", consultedAt = 2L))
        repository.saveOrUpdate(product(code = "111", name = "First Updated", consultedAt = 3L))

        repository.observeHistory().test {
            val items = awaitItem()
            assertEquals(2, items.size)
            assertEquals("111", items[0].code)
            assertEquals("First Updated", items[0].name)
            assertEquals("222", items[1].code)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun saveOrUpdate_whenExceedsLimit_removesOldest() = runTest {
        val repository = DefaultHistoryRepository(historyDao = FakeHistoryDao())
        repeat(DefaultHistoryRepository.MAX_HISTORY_ITEMS) { index ->
            val code = (1_000_000_000_000L + index).toString()
            repository.saveOrUpdate(
                product(
                    code = code,
                    name = "Product $index",
                    consultedAt = index.toLong(),
                ),
            )
        }
        repository.saveOrUpdate(
            product(
                code = "9999999999999",
                name = "Newest",
                consultedAt = 100L,
            ),
        )

        repository.observeHistory().test {
            val items = awaitItem()
            assertEquals(DefaultHistoryRepository.MAX_HISTORY_ITEMS, items.size)
            assertEquals("9999999999999", items.first().code)
            assertTrue(items.none { it.code == "1000000000000" })
            cancelAndIgnoreRemainingEvents()
        }
    }

    private fun product(
        code: String,
        name: String,
        consultedAt: Long,
    ): Product {
        return Product(
            code = code,
            name = name,
            brand = "Brand",
            allergens = "milk",
            ingredients = "sugar",
            nutriScore = 'B',
            consultedAtEpochMillis = consultedAt,
        )
    }
}
