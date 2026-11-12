package com.amdcruz.alertarotulo.history.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface HistoryDao {

    @Query("SELECT * FROM history_items ORDER BY consultedAtEpochMillis DESC")
    fun observeAll(): Flow<List<HistoryItemEntity>>

    @Query("SELECT * FROM history_items WHERE code = :code LIMIT 1")
    suspend fun getByCode(code: String): HistoryItemEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(item: HistoryItemEntity)

    @Query("SELECT COUNT(*) FROM history_items")
    suspend fun count(): Int

    @Query(
        """
        DELETE FROM history_items
        WHERE code IN (
            SELECT code FROM history_items
            ORDER BY consultedAtEpochMillis ASC
            LIMIT :limit
        )
        """,
    )
    suspend fun deleteOldest(limit: Int)

    @Query("DELETE FROM history_items")
    suspend fun clearAll()
}
