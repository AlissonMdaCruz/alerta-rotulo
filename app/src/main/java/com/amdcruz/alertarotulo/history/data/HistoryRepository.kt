package com.amdcruz.alertarotulo.history.data

import com.amdcruz.alertarotulo.history.data.model.HistoryItem
import com.amdcruz.alertarotulo.product.data.model.Product
import kotlinx.coroutines.flow.Flow

interface HistoryRepository {
    fun observeHistory(): Flow<List<HistoryItem>>
    suspend fun saveOrUpdate(product: Product)
    suspend fun getByCode(code: String): HistoryItem?
}
