package com.amdcruz.alertarotulo.scan.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.LiveRegionMode
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.heading
import androidx.compose.ui.semantics.liveRegion
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import com.amdcruz.alertarotulo.R
import com.amdcruz.alertarotulo.core.ui.components.PrimaryActionButton
import com.amdcruz.alertarotulo.core.ui.components.SecondaryActionButton

@Composable
fun CameraPermissionDeniedContent(
    onOpenSettings: () -> Unit,
    onOpenManualEntry: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val title = stringResource(R.string.camera_permission_denied_title)
    val body = stringResource(R.string.camera_permission_denied_body)
    Column(
        modifier = modifier.padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.semantics {
                heading()
                liveRegion = LiveRegionMode.Assertive
                contentDescription = "$title. $body"
            },
        )
        Text(
            text = body,
            style = MaterialTheme.typography.bodyLarge,
        )
        PrimaryActionButton(
            text = stringResource(R.string.action_enter_code_manually),
            onClick = onOpenManualEntry,
        )
        SecondaryActionButton(
            text = stringResource(R.string.action_open_app_settings),
            onClick = onOpenSettings,
        )
    }
}
