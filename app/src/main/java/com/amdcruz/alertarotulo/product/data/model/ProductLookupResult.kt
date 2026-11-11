package com.amdcruz.alertarotulo.product.data.model

sealed interface ProductLookupResult {
    data class Success(val product: Product) : ProductLookupResult
    data object NotFound : ProductLookupResult
    data object NetworkError : ProductLookupResult
}
