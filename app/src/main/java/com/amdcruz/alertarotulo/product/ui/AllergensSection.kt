package com.amdcruz.alertarotulo.product.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.WarningAmber
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.heading
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import com.amdcruz.alertarotulo.R
import com.amdcruz.alertarotulo.core.ui.components.SemanticBadge
import com.amdcruz.alertarotulo.core.ui.theme.AlertaRotuloThemeExtras

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun AllergensSection(
    allergens: String?,
    modifier: Modifier = Modifier,
) {
    val hasAllergens = !allergens.isNullOrBlank()
    val notInformed = stringResource(R.string.product_field_not_informed)
    val sectionDescription = if (hasAllergens) {
        stringResource(R.string.product_allergens_a11y, allergens)
    } else {
        stringResource(R.string.product_allergens_not_informed_a11y)
    }
    val semantic = AlertaRotuloThemeExtras.semanticColors
    val panelColors = if (hasAllergens) semantic.critical else semantic.safe
    val spacing = AlertaRotuloThemeExtras.spacing

    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(spacing.sm),
    ) {
        Text(
            text = stringResource(R.string.product_field_allergens),
            style = MaterialTheme.typography.headlineSmall,
            modifier = Modifier.semantics { heading() },
        )
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .semantics {
                    contentDescription = sectionDescription
                },
            shape = MaterialTheme.shapes.medium,
            color = panelColors.container,
            contentColor = panelColors.onContainer,
            border = BorderStroke(2.dp, panelColors.border),
        ) {
            Column(
                modifier = Modifier
                    .padding(spacing.md)
                    .clearAndSetSemantics { },
                verticalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                if (!hasAllergens) {
                    Text(
                        text = notInformed,
                        style = MaterialTheme.typography.bodyLarge,
                    )
                } else {
                    Text(
                        text = stringResource(R.string.product_allergens_declared_label),
                        style = MaterialTheme.typography.labelLarge,
                    )
                    FlowRow(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                    ) {
                        allergens
                            .split(',')
                            .map { it.trim() }
                            .filter { it.isNotEmpty() }
                            .forEach { allergen ->
                                SemanticBadge(
                                    text = allergen,
                                    colors = semantic.critical,
                                    icon = Icons.Filled.WarningAmber,
                                )
                            }
                    }
                }
            }
        }
    }
}
