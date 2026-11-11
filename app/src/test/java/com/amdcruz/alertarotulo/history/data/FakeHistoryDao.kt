package com.amdcruz.alertarotulo.history.data

import com.amdcruz.alertarotulo.history.data.local.HistoryDao
import com.amdcruz.alertarotulo.history.data.local.HistoryItemEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow

class FakeHistoryDao : HistoryDao {

    private val items = linkedMapOf<String, HistoryItemEntity>()
    private val itemsFlow = MutableStateFlow<List<HistoryItemEntity>>(emptyList())

    override fun observeAll(): Flow<List<HistoryItemEntity>> = itemsFlow

    override suspend fun getByCode(code: String): HistoryItemEntity? = items[code]

    override suspend fun upsert(item: HistoryItemEntity) {
        items.remove(item.code)
        items[item.code] = item
        publish()
    }

    override suspend fun count(): Int = items.size

    override suspend fun deleteOldest(limit: Int) {
        if (limit <= 0) {
            return
        }
        val oldestCodes = items.values
            .sortedBy { it.consultedAtEpochMillis }
            .take(limit)
            .map { it.code }
        oldestCodes.forEach { code -> items.remove(code) }
        publish()
    }

    override suspend fun clearAll() {
        items.clear()
        publish()
    }

    private fun publish() {
        itemsFlow.value = items.values
            .sortedByDescending { it.consultedAtEpochMillis }
    }
}
