package com.amdcruz.alertarotulo.history.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "history_items")
data class HistoryItemEntity(
    @PrimaryKey val code: String,
    val name: String?,
    val brand: String?,
    val allergens: String?,
    val ingredients: String?,
    val nutriScore: String?,
    val consultedAtEpochMillis: Long,
)
