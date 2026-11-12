package com.amdcruz.alertarotulo.scan.ui

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.provider.Settings
import android.view.HapticFeedbackConstants
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Keyboard
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.heading
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.amdcruz.alertarotulo.R
import com.amdcruz.alertarotulo.core.ui.components.PrimaryActionButton
import com.amdcruz.alertarotulo.core.ui.components.ScanViewfinderOverlay
import com.amdcruz.alertarotulo.core.ui.components.SecondaryActionButton
import com.amdcruz.alertarotulo.core.ui.theme.AlertaRotuloThemeExtras

@Composable
fun ScanCaptureScreen(
    viewModel: ScanViewModel,
    onBarcodeValidated: (String) -> Unit,
    onOpenManualEntry: () -> Unit,
    onOpenHistory: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val context = LocalContext.current
    val view = LocalView.current
    val lifecycleOwner = LocalLifecycleOwner.current
    val spacing = AlertaRotuloThemeExtras.spacing
    val scannedCode by viewModel.scannedCode.collectAsStateWithLifecycle()
    var hasCameraPermission by remember {
        mutableStateOf(
            ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA) ==
                PackageManager.PERMISSION_GRANTED,
        )
    }
    var permissionRequested by remember { mutableStateOf(false) }
    val cameraPreviewDescription = stringResource(R.string.scan_camera_preview_a11y)

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
        val haptic = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            HapticFeedbackConstants.CONFIRM
        } else {
            HapticFeedbackConstants.KEYBOARD_TAP
        }
        view.performHapticFeedback(haptic)
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
                        modifier = Modifier
                            .fillMaxSize()
                            .semantics {
                                contentDescription = cameraPreviewDescription
                            },
                    )
                    ScanViewfinderOverlay(modifier = Modifier.fillMaxSize())
                    Column(
                        modifier = Modifier
                            .align(Alignment.BottomCenter)
                            .fillMaxWidth()
                            .padding(spacing.screenMargin)
                            .background(
                                color = MaterialTheme.colorScheme.surfaceContainerLowest,
                                shape = MaterialTheme.shapes.large,
                            )
                            .border(
                                width = 1.5.dp,
                                color = MaterialTheme.colorScheme.outlineVariant,
                                shape = MaterialTheme.shapes.large,
                            )
                            .padding(spacing.md),
                        verticalArrangement = Arrangement.spacedBy(spacing.sm),
                    ) {
                        Text(
                            text = stringResource(R.string.scan_title),
                            style = MaterialTheme.typography.titleLarge,
                            modifier = Modifier.semantics { heading() },
                        )
                        Text(
                            text = stringResource(R.string.scan_instruction),
                            style = MaterialTheme.typography.bodyLarge,
                        )
                        SecondaryActionButton(
                            text = stringResource(R.string.action_enter_code_manually),
                            onClick = onOpenManualEntry,
                            icon = Icons.Filled.Keyboard,
                        )
                        SecondaryActionButton(
                            text = stringResource(R.string.action_open_history),
                            onClick = onOpenHistory,
                            icon = Icons.Filled.History,
                        )
                    }
                }
            }

            else -> {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding)
                        .verticalScroll(rememberScrollState()),
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
                    )
                    PrimaryActionButton(
                        text = stringResource(R.string.action_request_camera_permission),
                        onClick = { permissionLauncher.launch(Manifest.permission.CAMERA) },
                        modifier = Modifier.padding(horizontal = spacing.screenMarginWide),
                    )
                    SecondaryActionButton(
                        text = stringResource(R.string.action_open_history),
                        onClick = onOpenHistory,
                        icon = Icons.Filled.History,
                        modifier = Modifier.padding(spacing.screenMarginWide),
                    )
                }
            }
        }
    }
}
