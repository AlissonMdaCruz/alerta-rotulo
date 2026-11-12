package com.amdcruz.alertarotulo.scan

object BarcodeValidator {

    fun normalize(input: String): String = input.filter { it.isDigit() }

    fun validate(input: String): String? {
        val code = normalize(input)
        if (code.isEmpty()) {
            return null
        }
        return when (code.length) {
            EAN_8_LENGTH,
            UPC_A_LENGTH,
            EAN_13_LENGTH,
            -> code.takeIf { hasValidChecksum(it) }

            else -> null
        }
    }

    fun hasValidChecksum(code: String): Boolean {
        if (code.length !in setOf(EAN_8_LENGTH, UPC_A_LENGTH, EAN_13_LENGTH)) {
            return false
        }
        if (!code.all { it.isDigit() }) {
            return false
        }
        val expectedCheckDigit = computeCheckDigit(code.dropLast(1))
        return code.last().digitToInt() == expectedCheckDigit
    }

    private fun computeCheckDigit(dataDigits: String): Int {
        var sum = 0
        dataDigits.reversed().forEachIndexed { index, char ->
            val digit = char.digitToInt()
            sum += if (index % 2 == 0) digit * 3 else digit
        }
        return (10 - (sum % 10)) % 10
    }

    const val EAN_8_LENGTH: Int = 8
    const val UPC_A_LENGTH: Int = 12
    const val EAN_13_LENGTH: Int = 13
}
