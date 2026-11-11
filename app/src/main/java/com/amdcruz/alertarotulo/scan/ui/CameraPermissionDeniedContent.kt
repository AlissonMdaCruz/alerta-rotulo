package com.amdcruz.alertarotulo.scan.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.amdcruz.alertarotulo.R

@Composable
fun CameraPermissionDeniedContent(
    onOpenSettings: () -> Unit,
    onOpenManualEntry: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier.padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        Text(
            text = stringResource(R.string.camera_permission_denied_title),
            style = MaterialTheme.typography.headlineMedium,
        )
        Text(
            text = stringResource(R.string.camera_permission_denied_body),
            style = MaterialTheme.typography.bodyLarge,
        )
        Button(
            onClick = onOpenManualEntry,
            modifier = Modifier.fillMaxWidth(),
        ) {
            Text(stringResource(R.string.action_enter_code_manually))
        }
        OutlinedButton(
            onClick = onOpenSettings,
            modifier = Modifier.fillMaxWidth(),
        ) {
            Text(stringResource(R.string.action_open_app_settings))
        }
    }
}
