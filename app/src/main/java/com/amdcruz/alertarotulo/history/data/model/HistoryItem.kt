package com.amdcruz.alertarotulo.history.data.model

data class HistoryItem(
    val code: String,
    val name: String?,
    val brand: String?,
    val allergens: String?,
    val ingredients: String?,
    val nutriScore: Char?,
    val consultedAtEpochMillis: Long,
)
