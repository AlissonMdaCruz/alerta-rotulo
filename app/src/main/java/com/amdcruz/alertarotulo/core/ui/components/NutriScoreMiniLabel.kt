package com.amdcruz.alertarotulo.core.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.amdcruz.alertarotulo.core.ui.theme.AlertaRotuloThemeExtras
import com.amdcruz.alertarotulo.core.ui.theme.forGrade

@Composable
fun NutriScoreMiniLabel(
    grade: Char,
    modifier: Modifier = Modifier,
) {
    val colors = AlertaRotuloThemeExtras.nutriScoreColors.forGrade(grade)
    Surface(
        modifier = modifier,
        shape = MaterialTheme.shapes.small,
        color = colors.container,
        contentColor = colors.onContainer,
        border = BorderStroke(1.5.dp, colors.border),
    ) {
        Text(
            text = grade.uppercaseChar().toString(),
            style = MaterialTheme.typography.labelLarge,
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
        )
    }
}
