package com.amdcruz.alertarotulo.product.data

import com.amdcruz.alertarotulo.product.data.mapper.OffProductMapper
import com.amdcruz.alertarotulo.product.data.model.ProductLookupResult
import com.amdcruz.alertarotulo.product.data.remote.OpenFoodFactsApi
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton
import retrofit2.HttpException

@Singleton
class DefaultProductRepository @Inject constructor(
    private val openFoodFactsApi: OpenFoodFactsApi,
) : ProductRepository {

    override suspend fun lookup(code: String): ProductLookupResult {
        val normalizedCode = code.filter { it.isDigit() }
        if (normalizedCode.isEmpty()) {
            return ProductLookupResult.NotFound
        }
        return try {
            val response = openFoodFactsApi.getProduct(code = normalizedCode)
            val product = OffProductMapper.map(
                response = response,
                fallbackCode = normalizedCode,
                consultedAtEpochMillis = System.currentTimeMillis(),
            )
            if (product == null) {
                ProductLookupResult.NotFound
            } else {
                ProductLookupResult.Success(product)
            }
        } catch (_: IOException) {
            ProductLookupResult.NetworkError
        } catch (_: HttpException) {
            ProductLookupResult.NetworkError
        }
    }
}
