package com.amdcruz.alertarotulo.product.data.remote

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class OffProductResponseDto(
    val status: Int,
    val code: String? = null,
    val product: OffProductDto? = null,
)

@Serializable
data class OffProductDto(
    @SerialName("product_name")
    val productName: String? = null,
    val brands: String? = null,
    val allergens: String? = null,
    @SerialName("allergens_tags")
    val allergensTags: List<String>? = null,
    @SerialName("ingredients_text")
    val ingredientsText: String? = null,
    @SerialName("nutriscore_grade")
    val nutriscoreGrade: String? = null,
)
