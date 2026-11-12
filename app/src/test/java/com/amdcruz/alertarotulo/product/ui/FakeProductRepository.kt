package com.amdcruz.alertarotulo.product.ui

import com.amdcruz.alertarotulo.product.data.ProductRepository
import com.amdcruz.alertarotulo.product.data.model.ProductLookupResult

class FakeProductRepository(
    private val resultProvider: (String) -> ProductLookupResult,
) : ProductRepository {

    override suspend fun lookup(code: String): ProductLookupResult {
        return resultProvider(code.filter { it.isDigit() })
    }
}
