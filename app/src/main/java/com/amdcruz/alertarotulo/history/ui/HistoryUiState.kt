package com.amdcruz.alertarotulo.history.ui

import com.amdcruz.alertarotulo.history.data.model.HistoryItem

data class HistoryUiState(
    val items: List<HistoryItem> = emptyList(),
)
