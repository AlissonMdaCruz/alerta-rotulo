package com.amdcruz.alertarotulo.scan.ui

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.provider.Settings
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.amdcruz.alertarotulo.R

@Composable
fun ScanCaptureScreen(
    viewModel: ScanViewModel,
    onBarcodeValidated: (String) -> Unit,
    onOpenManualEntry: () -> Unit,
    onOpenHistory: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    val scannedCode by viewModel.scannedCode.collectAsStateWithLifecycle()
    var hasCameraPermission by remember {
        mutableStateOf(
            ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA) ==
                PackageManager.PERMISSION_GRANTED,
        )
    }
    var permissionRequested by remember { mutableStateOf(false) }

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
    ) { granted ->
        permissionRequested = true
        hasCameraPermission = granted
    }

    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_RESUME) {
                hasCameraPermission =
                    ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA) ==
                        PackageManager.PERMISSION_GRANTED
                viewModel.resetForNewScan()
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose { lifecycleOwner.lifecycle.removeObserver(observer) }
    }

    LaunchedEffect(hasCameraPermission, permissionRequested) {
        if (!hasCameraPermission && !permissionRequested) {
            permissionLauncher.launch(Manifest.permission.CAMERA)
        }
    }

    LaunchedEffect(scannedCode) {
        val code = scannedCode ?: return@LaunchedEffect
        onBarcodeValidated(code)
        viewModel.consumeScannedCode()
    }

    Scaffold(modifier = modifier.fillMaxSize()) { innerPadding ->
        when {
            hasCameraPermission -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding),
                ) {
                    CameraPreview(
                        onBarcodeDetected = { rawValue ->
                            viewModel.onBarcodeDetected(rawValue)
                        },
                        modifier = Modifier.fillMaxSize(),
                    )
                    Column(
                        modifier = Modifier
                            .align(Alignment.BottomCenter)
                            .fillMaxWidth()
                            .background(MaterialTheme.colorScheme.surface.copy(alpha = 0.92f))
                            .padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                    ) {
                        Text(
                            text = stringResource(R.string.scan_instruction),
                            style = MaterialTheme.typography.bodyLarge,
                        )
                        OutlinedButton(
                            onClick = onOpenManualEntry,
                            modifier = Modifier.fillMaxWidth(),
                        ) {
                            Text(stringResource(R.string.action_enter_code_manually))
                        }
                        OutlinedButton(
                            onClick = onOpenHistory,
                            modifier = Modifier.fillMaxWidth(),
                        ) {
                            Text(stringResource(R.string.action_open_history))
                        }
                    }
                }
            }

            else -> {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding),
                ) {
                    CameraPermissionDeniedContent(
                        onOpenSettings = {
                            val intent = Intent(
                                Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                                Uri.fromParts("package", context.packageName, null),
                            )
                            context.startActivity(intent)
                        },
                        onOpenManualEntry = onOpenManualEntry,
                        modifier = Modifier.weight(1f),
                    )
                    Button(
                        onClick = { permissionLauncher.launch(Manifest.permission.CAMERA) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 24.dp),
                    ) {
                        Text(stringResource(R.string.action_request_camera_permission))
                    }
                    OutlinedButton(
                        onClick = onOpenHistory,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(24.dp),
                    ) {
                        Text(stringResource(R.string.action_open_history))
                    }
                }
            }
        }
    }
}
