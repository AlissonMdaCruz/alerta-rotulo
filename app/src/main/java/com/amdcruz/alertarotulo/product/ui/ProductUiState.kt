package com.amdcruz.alertarotulo.product.ui

import com.amdcruz.alertarotulo.product.data.model.Product

sealed interface ProductUiState {
    data object Idle : ProductUiState
    data object Loading : ProductUiState
    data class Success(val product: Product) : ProductUiState
    data class NotFound(val code: String) : ProductUiState
    data class NetworkError(val code: String) : ProductUiState
}
