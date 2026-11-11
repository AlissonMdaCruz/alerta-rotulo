package com.amdcruz.alertarotulo.scan.ui

import androidx.annotation.OptIn
import androidx.camera.core.ExperimentalGetImage
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import com.google.mlkit.vision.barcode.BarcodeScanner
import com.google.mlkit.vision.barcode.BarcodeScannerOptions
import com.google.mlkit.vision.barcode.BarcodeScanning
import com.google.mlkit.vision.barcode.common.Barcode
import com.google.mlkit.vision.common.InputImage
import java.util.concurrent.atomic.AtomicBoolean

class BarcodeImageAnalyzer(
    private val onBarcodeDetected: (String) -> Unit,
) : ImageAnalysis.Analyzer {

    private val scanner: BarcodeScanner = BarcodeScanning.getClient(
        BarcodeScannerOptions.Builder()
            .setBarcodeFormats(
                Barcode.FORMAT_EAN_13,
                Barcode.FORMAT_EAN_8,
                Barcode.FORMAT_UPC_A,
            )
            .build(),
    )
    private val isBusy = AtomicBoolean(false)

    @OptIn(ExperimentalGetImage::class)
    override fun analyze(imageProxy: ImageProxy) {
        if (!isBusy.compareAndSet(false, true)) {
            imageProxy.close()
            return
        }
        val mediaImage = imageProxy.image
        if (mediaImage == null) {
            isBusy.set(false)
            imageProxy.close()
            return
        }
        val inputImage = InputImage.fromMediaImage(
            mediaImage,
            imageProxy.imageInfo.rotationDegrees,
        )
        scanner.process(inputImage)
            .addOnSuccessListener { barcodes ->
                val value = barcodes
                    .asSequence()
                    .mapNotNull { barcode -> barcode.rawValue }
                    .firstOrNull()
                if (value != null) {
                    onBarcodeDetected(value)
                }
            }
            .addOnCompleteListener {
                isBusy.set(false)
                imageProxy.close()
            }
    }

    fun close() {
        scanner.close()
    }
}
