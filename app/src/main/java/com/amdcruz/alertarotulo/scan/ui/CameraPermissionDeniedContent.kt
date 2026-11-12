package com.amdcruz.alertarotulo.scan.ui

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Keyboard
import androidx.compose.material.icons.filled.NoPhotography
import androidx.compose.material.icons.filled.Settings
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.amdcruz.alertarotulo.R
import com.amdcruz.alertarotulo.core.ui.components.PrimaryActionButton
import com.amdcruz.alertarotulo.core.ui.components.SecondaryActionButton
import com.amdcruz.alertarotulo.core.ui.components.StatusStateContent

@Composable
fun CameraPermissionDeniedContent(
    onOpenSettings: () -> Unit,
    onOpenManualEntry: () -> Unit,
    modifier: Modifier = Modifier,
) {
    StatusStateContent(
        title = stringResource(R.string.camera_permission_denied_title),
        body = stringResource(R.string.camera_permission_denied_body),
        icon = Icons.Filled.NoPhotography,
        modifier = modifier,
        assertiveLiveRegion = true,
    ) {
        PrimaryActionButton(
            text = stringResource(R.string.action_enter_code_manually),
            onClick = onOpenManualEntry,
            icon = Icons.Filled.Keyboard,
        )
        SecondaryActionButton(
            text = stringResource(R.string.action_open_app_settings),
            onClick = onOpenSettings,
            icon = Icons.Filled.Settings,
        )
    }
}
