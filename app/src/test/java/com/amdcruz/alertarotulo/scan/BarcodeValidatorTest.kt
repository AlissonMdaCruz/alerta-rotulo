package com.amdcruz.alertarotulo.scan

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

class BarcodeValidatorTest {

    @Test
    fun validate_ean13Nutella_returnsNormalizedCode() {
        val actual = BarcodeValidator.validate("3017620422003")
        assertEquals("3017620422003", actual)
    }

    @Test
    fun validate_ean13WithSpacesAndHyphens_normalizesAndAccepts() {
        val actual = BarcodeValidator.validate("3017-6204 2200 3")
        assertEquals("3017620422003", actual)
    }

    @Test
    fun validate_invalidChecksum_returnsNull() {
        val actual = BarcodeValidator.validate("3017620422004")
        assertNull(actual)
    }

    @Test
    fun validate_empty_returnsNull() {
        assertNull(BarcodeValidator.validate(""))
        assertNull(BarcodeValidator.validate("   "))
    }

    @Test
    fun validate_unsupportedLength_returnsNull() {
        assertNull(BarcodeValidator.validate("1234567"))
        assertNull(BarcodeValidator.validate("12345678901"))
        assertNull(BarcodeValidator.validate("12345678901234"))
    }

    @Test
    fun validate_upcA_withValidChecksum_returnsCode() {
        // UPC-A for Coca-Cola classic commonly listed as 049000028911
        val code = "049000028911"
        assertTrue(BarcodeValidator.hasValidChecksum(code))
        assertEquals(code, BarcodeValidator.validate(code))
    }

    @Test
    fun validate_ean8_withValidChecksum_returnsCode() {
        val code = "96385074"
        assertTrue(BarcodeValidator.hasValidChecksum(code))
        assertEquals(code, BarcodeValidator.validate(code))
    }
}
