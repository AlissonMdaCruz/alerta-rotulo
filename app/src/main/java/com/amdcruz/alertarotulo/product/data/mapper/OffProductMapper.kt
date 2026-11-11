package com.amdcruz.alertarotulo.product.data.mapper

import com.amdcruz.alertarotulo.product.data.model.Product
import com.amdcruz.alertarotulo.product.data.remote.OffProductDto
import com.amdcruz.alertarotulo.product.data.remote.OffProductResponseDto

object OffProductMapper {

    fun map(
        response: OffProductResponseDto,
        fallbackCode: String,
        consultedAtEpochMillis: Long,
    ): Product? {
        if (response.status != PRODUCT_FOUND_STATUS || response.product == null) {
            return null
        }
        return mapProduct(
            dto = response.product,
            code = response.code?.takeIf { it.isNotBlank() } ?: fallbackCode,
            consultedAtEpochMillis = consultedAtEpochMillis,
        )
    }

    fun mapProduct(
        dto: OffProductDto,
        code: String,
        consultedAtEpochMillis: Long,
    ): Product {
        return Product(
            code = code,
            name = dto.productName.nullIfBlank(),
            brand = dto.brands.nullIfBlank(),
            allergens = resolveAllergens(dto),
            ingredients = dto.ingredientsText.nullIfBlank(),
            nutriScore = dto.nutriscoreGrade.toNutriScoreLetter(),
            consultedAtEpochMillis = consultedAtEpochMillis,
        )
    }

    private fun resolveAllergens(dto: OffProductDto): String? {
        val allergensText = dto.allergens.nullIfBlank()
        if (allergensText != null) {
            return allergensText
        }
        val tags = dto.allergensTags.orEmpty()
            .mapNotNull { tag -> tag.substringAfter(':', missingDelimiterValue = tag).nullIfBlank() }
            .filter { it.isNotEmpty() }
        return tags.takeIf { it.isNotEmpty() }?.joinToString(separator = ", ")
    }

    private fun String?.toNutriScoreLetter(): Char? {
        val grade = this?.trim()?.lowercase().orEmpty()
        if (grade.length != 1) {
            return null
        }
        val letter = grade[0]
        return if (letter in 'a'..'e') letter.uppercaseChar() else null
    }

    private fun String?.nullIfBlank(): String? = this?.trim()?.takeIf { it.isNotEmpty() }

    private const val PRODUCT_FOUND_STATUS: Int = 1
}
