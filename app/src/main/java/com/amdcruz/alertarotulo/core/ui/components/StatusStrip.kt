package com.amdcruz.alertarotulo.core.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.heading
import androidx.compose.ui.semantics.semantics
import com.amdcruz.alertarotulo.core.ui.theme.AlertaRotuloThemeExtras
import com.amdcruz.alertarotulo.core.ui.theme.SemanticColorPair

@Composable
fun StatusStrip(
    label: String,
    colors: SemanticColorPair,
    modifier: Modifier = Modifier,
) {
    val spacing = AlertaRotuloThemeExtras.spacing
    Column(modifier = modifier.fillMaxWidth()) {
        Spacer(
            modifier = Modifier
                .fillMaxWidth()
                .height(spacing.statusStripHeight)
                .background(colors.border),
        )
        Text(
            text = label,
            style = MaterialTheme.typography.labelLarge,
            color = colors.onContainer,
            modifier = Modifier
                .fillMaxWidth()
                .background(colors.container)
                .padding(horizontal = spacing.md, vertical = spacing.sm)
                .semantics { heading() },
        )
    }
}
