package com.amdcruz.alertarotulo.product.ui

import com.amdcruz.alertarotulo.history.data.HistoryRepository
import com.amdcruz.alertarotulo.history.data.mapper.HistoryMapper
import com.amdcruz.alertarotulo.history.data.model.HistoryItem
import com.amdcruz.alertarotulo.product.data.model.Product
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow

class FakeHistoryRepository : HistoryRepository {

    private val items = linkedMapOf<String, HistoryItem>()
    private val itemsFlow = MutableStateFlow<List<HistoryItem>>(emptyList())
    val savedProducts = mutableListOf<Product>()

    override fun observeHistory(): Flow<List<HistoryItem>> = itemsFlow

    override suspend fun saveOrUpdate(product: Product) {
        savedProducts += product
        items[product.code] = HistoryMapper.toModel(
            HistoryMapper.toEntity(product),
        )
        publish()
    }

    override suspend fun getByCode(code: String): HistoryItem? = items[code]

    private fun publish() {
        itemsFlow.value = items.values
            .sortedByDescending { it.consultedAtEpochMillis }
    }
}
