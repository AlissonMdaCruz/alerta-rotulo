package com.amdcruz.alertarotulo.product.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import com.amdcruz.alertarotulo.R
import com.amdcruz.alertarotulo.core.ui.theme.AlertaRotuloThemeExtras
import com.amdcruz.alertarotulo.core.ui.theme.forGrade

@Composable
fun NutriScoreBadge(
    nutriScore: Char?,
    modifier: Modifier = Modifier,
) {
    val label = nutriScore?.uppercaseChar()?.toString()
        ?: stringResource(R.string.product_field_not_informed)
    val description = if (nutriScore != null) {
        stringResource(R.string.product_nutriscore_a11y, nutriScore.uppercaseChar())
    } else {
        stringResource(R.string.product_nutriscore_not_informed_a11y)
    }
    val colors = AlertaRotuloThemeExtras.nutriScoreColors.forGrade(nutriScore)
    Surface(
        modifier = modifier.semantics {
            contentDescription = description
        },
        shape = MaterialTheme.shapes.small,
        color = colors.container,
        contentColor = colors.onContainer,
        border = BorderStroke(2.dp, colors.border),
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.headlineSmall,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
        )
    }
}
