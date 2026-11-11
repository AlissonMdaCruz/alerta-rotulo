package com.amdcruz.alertarotulo.product.data.model

data class Product(
    val code: String,
    val name: String?,
    val brand: String?,
    val allergens: String?,
    val ingredients: String?,
    val nutriScore: Char?,
    val consultedAtEpochMillis: Long,
)
