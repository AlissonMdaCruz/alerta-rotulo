package com.amdcruz.alertarotulo.product.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
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
    val containerColor = if (hasAllergens) {
        MaterialTheme.colorScheme.errorContainer
    } else {
        MaterialTheme.colorScheme.surfaceVariant
    }
    val contentColor = if (hasAllergens) {
        MaterialTheme.colorScheme.onErrorContainer
    } else {
        MaterialTheme.colorScheme.onSurfaceVariant
    }
    val borderColor = if (hasAllergens) {
        MaterialTheme.colorScheme.error
    } else {
        MaterialTheme.colorScheme.outline
    }

    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(8.dp),
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
            shape = RoundedCornerShape(12.dp),
            color = containerColor,
            contentColor = contentColor,
            border = BorderStroke(2.dp, borderColor),
        ) {
            Column(
                modifier = Modifier
                    .padding(16.dp)
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
                                AllergenBadge(text = allergen)
                            }
                    }
                }
            }
        }
    }
}

@Composable
private fun AllergenBadge(
    text: String,
    modifier: Modifier = Modifier,
) {
    Surface(
        modifier = modifier.heightIn(min = 36.dp),
        shape = RoundedCornerShape(8.dp),
        color = MaterialTheme.colorScheme.error,
        contentColor = MaterialTheme.colorScheme.onError,
        border = BorderStroke(2.dp, MaterialTheme.colorScheme.error),
    ) {
        Text(
            text = text.uppercase(),
            style = MaterialTheme.typography.labelLarge,
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
        )
    }
}
