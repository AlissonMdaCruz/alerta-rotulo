package com.amdcruz.alertarotulo.history.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle

@Composable
fun HistoryRoute(
    onItemClick: (String) -> Unit,
    onStartNewSearch: () -> Unit,
    onBack: () -> Unit,
    viewModel: HistoryViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    HistoryListScreen(
        uiState = uiState,
        onItemClick = onItemClick,
        onStartNewSearch = onStartNewSearch,
        onBack = onBack,
    )
}
