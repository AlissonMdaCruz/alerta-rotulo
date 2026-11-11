package com.amdcruz.alertarotulo.history.data

import com.amdcruz.alertarotulo.history.data.local.HistoryDao
import com.amdcruz.alertarotulo.history.data.mapper.HistoryMapper
import com.amdcruz.alertarotulo.history.data.model.HistoryItem
import com.amdcruz.alertarotulo.product.data.model.Product
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

@Singleton
class DefaultHistoryRepository @Inject constructor(
    private val historyDao: HistoryDao,
) : HistoryRepository {

    override fun observeHistory(): Flow<List<HistoryItem>> {
        return historyDao.observeAll().map { entities ->
            entities.map(HistoryMapper::toModel)
        }
    }

    override suspend fun saveOrUpdate(product: Product) {
        historyDao.upsert(HistoryMapper.toEntity(product))
        val overflow = historyDao.count() - MAX_HISTORY_ITEMS
        if (overflow > 0) {
            historyDao.deleteOldest(overflow)
        }
    }

    override suspend fun getByCode(code: String): HistoryItem? {
        return historyDao.getByCode(code)?.let(HistoryMapper::toModel)
    }

    companion object {
        const val MAX_HISTORY_ITEMS: Int = 20
    }
}
