package com.amdcruz.alertarotulo.scan.ui

import android.util.Log
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.lifecycle.compose.LocalLifecycleOwner
import java.util.concurrent.Executors
import java.util.concurrent.atomic.AtomicReference

@Composable
fun CameraPreview(
    onBarcodeDetected: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    val analysisExecutor = remember { Executors.newSingleThreadExecutor() }
    val callbackRef = remember { AtomicReference(onBarcodeDetected) }
    callbackRef.set(onBarcodeDetected)
    val barcodeAnalyzer = remember {
        BarcodeImageAnalyzer { value -> callbackRef.get().invoke(value) }
    }
    var previewView by remember { mutableStateOf<PreviewView?>(null) }

    DisposableEffect(lifecycleOwner, previewView) {
        val boundPreviewView = previewView
        if (boundPreviewView == null) {
            return@DisposableEffect onDispose { }
        }
        val cameraProviderFuture = ProcessCameraProvider.getInstance(context)
        val mainExecutor = ContextCompat.getMainExecutor(context)
        val bindListener = Runnable {
            try {
                val cameraProvider = cameraProviderFuture.get()
                val preview = Preview.Builder().build().also { previewUseCase ->
                    previewUseCase.surfaceProvider = boundPreviewView.surfaceProvider
                }
                val imageAnalysis = ImageAnalysis.Builder()
                    .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                    .build()
                    .also { analysis ->
                        analysis.setAnalyzer(analysisExecutor, barcodeAnalyzer)
                    }
                cameraProvider.unbindAll()
                cameraProvider.bindToLifecycle(
                    lifecycleOwner,
                    CameraSelector.DEFAULT_BACK_CAMERA,
                    preview,
                    imageAnalysis,
                )
            } catch (error: Exception) {
                Log.e(TAG, "Failed to bind camera use cases", error)
            }
        }
        cameraProviderFuture.addListener(bindListener, mainExecutor)
        onDispose {
            try {
                cameraProviderFuture.get().unbindAll()
            } catch (error: Exception) {
                Log.e(TAG, "Failed to unbind camera use cases", error)
            }
        }
    }

    DisposableEffect(Unit) {
        onDispose {
            barcodeAnalyzer.close()
            analysisExecutor.shutdown()
        }
    }

    AndroidView(
        factory = { viewContext ->
            PreviewView(viewContext).apply {
                scaleType = PreviewView.ScaleType.FILL_CENTER
                previewView = this
            }
        },
        modifier = modifier,
    )
}

private const val TAG: String = "CameraPreview"
