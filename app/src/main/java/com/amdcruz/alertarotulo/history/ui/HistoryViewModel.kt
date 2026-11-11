package com.amdcruz.alertarotulo.history.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.amdcruz.alertarotulo.history.data.HistoryRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

@HiltViewModel
class HistoryViewModel @Inject constructor(
    historyRepository: HistoryRepository,
) : ViewModel() {

    val uiState: StateFlow<HistoryUiState> = historyRepository.observeHistory()
        .map { items -> HistoryUiState(items = items) }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = HistoryUiState(),
        )
}
