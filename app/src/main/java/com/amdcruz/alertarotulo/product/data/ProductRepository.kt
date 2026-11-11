package com.amdcruz.alertarotulo.product.data

import com.amdcruz.alertarotulo.product.data.model.ProductLookupResult

interface ProductRepository {
    suspend fun lookup(code: String): ProductLookupResult
}
