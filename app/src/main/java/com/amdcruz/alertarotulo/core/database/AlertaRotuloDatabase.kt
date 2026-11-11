package com.amdcruz.alertarotulo.core.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.amdcruz.alertarotulo.history.data.local.HistoryDao
import com.amdcruz.alertarotulo.history.data.local.HistoryItemEntity

@Database(
    entities = [HistoryItemEntity::class],
    version = 1,
    exportSchema = false,
)
abstract class AlertaRotuloDatabase : RoomDatabase() {
    abstract fun historyDao(): HistoryDao
}
