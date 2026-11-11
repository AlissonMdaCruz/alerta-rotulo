package com.amdcruz.alertarotulo.product.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
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

@Composable
fun NutriScoreBadge(
    nutriScore: Char?,
    modifier: Modifier = Modifier,
) {
    val label = nutriScore?.toString()
        ?: stringResource(R.string.product_field_not_informed)
    val description = if (nutriScore != null) {
        stringResource(R.string.product_nutriscore_a11y, nutriScore)
    } else {
        stringResource(R.string.product_nutriscore_not_informed_a11y)
    }
    Surface(
        modifier = modifier.semantics {
            contentDescription = description
        },
        shape = RoundedCornerShape(8.dp),
        color = MaterialTheme.colorScheme.primaryContainer,
        contentColor = MaterialTheme.colorScheme.onPrimaryContainer,
        border = BorderStroke(2.dp, MaterialTheme.colorScheme.primary),
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.headlineSmall,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
        )
    }
}
