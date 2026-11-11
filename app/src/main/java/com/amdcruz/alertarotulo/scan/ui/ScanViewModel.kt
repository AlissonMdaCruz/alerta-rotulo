package com.amdcruz.alertarotulo.scan.ui

import androidx.lifecycle.ViewModel
import com.amdcruz.alertarotulo.scan.BarcodeValidator
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

@HiltViewModel
class ScanViewModel @Inject constructor() : ViewModel() {

    private val _scannedCode = MutableStateFlow<String?>(null)
    val scannedCode: StateFlow<String?> = _scannedCode.asStateFlow()

    private var lockedCode: String? = null

    fun onBarcodeDetected(rawValue: String): Boolean {
        if (_scannedCode.value != null) {
            return false
        }
        val validated = BarcodeValidator.validate(rawValue) ?: return false
        if (validated == lockedCode) {
            return false
        }
        lockedCode = validated
        _scannedCode.value = validated
        return true
    }

    fun consumeScannedCode() {
        _scannedCode.value = null
    }

    fun resetForNewScan() {
        lockedCode = null
        _scannedCode.value = null
    }
}
