package com.amdcruz.alertarotulo.product.data.mapper

import com.amdcruz.alertarotulo.product.data.remote.OffProductDto
import com.amdcruz.alertarotulo.product.data.remote.OffProductResponseDto
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class OffProductMapperTest {

    @Test
    fun map_fullProduct_mapsAllFields() {
        val response = OffProductResponseDto(
            status = 1,
            code = "3017620422003",
            product = OffProductDto(
                productName = "Nutella",
                brands = "Ferrero",
                allergens = "milk, nuts, soybeans",
                allergensTags = listOf("en:milk", "en:nuts", "en:soybeans"),
                ingredientsText = "sugar, palm oil",
                nutriscoreGrade = "e",
            ),
        )

        val actual = OffProductMapper.map(
            response = response,
            fallbackCode = "000",
            consultedAtEpochMillis = 1_700_000_000_000L,
        )

        requireNotNull(actual)
        assertEquals("3017620422003", actual.code)
        assertEquals("Nutella", actual.name)
        assertEquals("Ferrero", actual.brand)
        assertEquals("milk, nuts, soybeans", actual.allergens)
        assertEquals("sugar, palm oil", actual.ingredients)
        assertEquals('E', actual.nutriScore)
        assertEquals(1_700_000_000_000L, actual.consultedAtEpochMillis)
    }

    @Test
    fun map_partialProduct_leavesMissingFieldsNull() {
        val response = OffProductResponseDto(
            status = 1,
            code = "1234567890123",
            product = OffProductDto(
                productName = "Partial Product",
                brands = null,
                allergens = "  ",
                allergensTags = emptyList(),
                ingredientsText = null,
                nutriscoreGrade = "unknown",
            ),
        )

        val actual = OffProductMapper.map(
            response = response,
            fallbackCode = "1234567890123",
            consultedAtEpochMillis = 10L,
        )

        requireNotNull(actual)
        assertEquals("Partial Product", actual.name)
        assertNull(actual.brand)
        assertNull(actual.allergens)
        assertNull(actual.ingredients)
        assertNull(actual.nutriScore)
    }

    @Test
    fun map_productNotFound_returnsNull() {
        val response = OffProductResponseDto(
            status = 0,
            code = "0000000000000",
            product = null,
        )

        val actual = OffProductMapper.map(
            response = response,
            fallbackCode = "0000000000000",
            consultedAtEpochMillis = 10L,
        )

        assertNull(actual)
    }

    @Test
    fun map_allergensFromTagsWhenTextMissing() {
        val response = OffProductResponseDto(
            status = 1,
            code = "111",
            product = OffProductDto(
                productName = "Tagged",
                allergens = null,
                allergensTags = listOf("en:gluten", "en:eggs"),
            ),
        )

        val actual = OffProductMapper.map(
            response = response,
            fallbackCode = "111",
            consultedAtEpochMillis = 10L,
        )

        requireNotNull(actual)
        assertEquals("gluten, eggs", actual.allergens)
    }
}
