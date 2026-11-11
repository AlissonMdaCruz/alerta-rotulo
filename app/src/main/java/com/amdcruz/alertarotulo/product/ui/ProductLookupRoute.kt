package com.amdcruz.alertarotulo.product.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle

@Composable
fun ProductLookupRoute(
    code: String,
    onBack: () -> Unit,
    onNewSearch: () -> Unit,
    viewModel: ProductViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    LaunchedEffect(code) {
        viewModel.lookup(code)
    }
    ProductScreen(
        uiState = uiState,
        onRetry = viewModel::retry,
        onNewSearch = onNewSearch,
        onBack = onBack,
    )
}
