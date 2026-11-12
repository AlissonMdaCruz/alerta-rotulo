package com.amdcruz.alertarotulo.product.data.remote

import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface OpenFoodFactsApi {

    @GET("api/v2/product/{code}")
    suspend fun getProduct(
        @Path("code") code: String,
        @Query("fields") fields: String = DEFAULT_FIELDS,
    ): OffProductResponseDto

    companion object {
        const val DEFAULT_FIELDS: String =
            "code,product_name,brands,allergens,allergens_tags,ingredients_text,nutriscore_grade"
    }
}
