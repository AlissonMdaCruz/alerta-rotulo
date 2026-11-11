package com.amdcruz.alertarotulo.scan.ui

import androidx.compose.runtime.Composable
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel

@Composable
fun ScanCaptureRoute(
    onBarcodeValidated: (String) -> Unit,
    onOpenManualEntry: () -> Unit,
    onOpenHistory: () -> Unit,
    viewModel: ScanViewModel = hiltViewModel(),
) {
    ScanCaptureScreen(
        viewModel = viewModel,
        onBarcodeValidated = onBarcodeValidated,
        onOpenManualEntry = onOpenManualEntry,
        onOpenHistory = onOpenHistory,
    )
}
