package com.amdcruz.alertarotulo.product.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CloudOff
import androidx.compose.material.icons.filled.SearchOff
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.heading
import androidx.compose.ui.semantics.semantics
import com.amdcruz.alertarotulo.R
import com.amdcruz.alertarotulo.core.ui.components.AppTopBar
import com.amdcruz.alertarotulo.core.ui.components.PrimaryActionButton
import com.amdcruz.alertarotulo.core.ui.components.ProductInfoCard
import com.amdcruz.alertarotulo.core.ui.components.SecondaryActionButton
import com.amdcruz.alertarotulo.core.ui.components.StatusStateContent
import com.amdcruz.alertarotulo.core.ui.components.StatusStrip
import com.amdcruz.alertarotulo.core.ui.theme.AlertaRotuloThemeExtras
import com.amdcruz.alertarotulo.product.data.model.Product

@Composable
fun ProductScreen(
    uiState: ProductUiState,
    onRetry: () -> Unit,
    onNewSearch: () -> Unit,
    onBack: () -> Unit,
    modifier: Modifier = Modifier,
    isFromHistory: Boolean = false,
) {
    val title = when (uiState) {
        is ProductUiState.NotFound -> stringResource(R.string.product_not_found_title)
        is ProductUiState.NetworkError -> stringResource(R.string.product_network_error_title)
        else -> stringResource(R.string.product_result_title)
    }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            AppTopBar(
                title = title,
                onNavigateBack = onBack,
            )
        },
    ) { innerPadding ->
        when (uiState) {
            ProductUiState.Idle,
            ProductUiState.Loading,
            -> StatusStateContent(
                title = stringResource(R.string.product_loading),
                body = stringResource(R.string.product_loading_body),
                isLoading = true,
                assertiveLiveRegion = true,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
            )

            is ProductUiState.Success -> SuccessContent(
                product = uiState.product,
                isFromHistory = isFromHistory,
                onNewSearch = onNewSearch,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
            )

            is ProductUiState.NotFound -> StatusStateContent(
                title = stringResource(R.string.product_not_found_title),
                body = stringResource(R.string.product_not_found_body, uiState.code),
                icon = Icons.Filled.SearchOff,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
            ) {
                PrimaryActionButton(
                    text = stringResource(R.string.action_new_search),
                    onClick = onNewSearch,
                )
            }

            is ProductUiState.NetworkError -> StatusStateContent(
                title = stringResource(R.string.product_network_error_title),
                body = stringResource(R.string.product_network_error_body, uiState.code),
                icon = Icons.Filled.CloudOff,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
            ) {
                PrimaryActionButton(
                    text = stringResource(R.string.action_retry),
                    onClick = onRetry,
                )
                SecondaryActionButton(
                    text = stringResource(R.string.action_new_search),
                    onClick = onNewSearch,
                )
            }
        }
    }
}

@Composable
private fun SuccessContent(
    product: Product,
    isFromHistory: Boolean,
    onNewSearch: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val spacing = AlertaRotuloThemeExtras.spacing
    val semantic = AlertaRotuloThemeExtras.semanticColors
    val hasAllergens = !product.allergens.isNullOrBlank()
    val statusColors = if (hasAllergens) semantic.critical else semantic.safe
    val statusLabel = if (hasAllergens) {
        stringResource(R.string.product_status_allergens_declared)
    } else {
        stringResource(R.string.product_status_allergens_not_informed)
    }

    Column(
        modifier = modifier
            .verticalScroll(rememberScrollState())
            .padding(spacing.screenMarginWide),
        verticalArrangement = Arrangement.spacedBy(spacing.md),
    ) {
        if (isFromHistory) {
            Text(
                text = stringResource(R.string.product_saved_lookup_label),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
        ProductInfoCard {
            StatusStrip(label = statusLabel, colors = statusColors)
            Column(
                modifier = Modifier.padding(spacing.md),
                verticalArrangement = Arrangement.spacedBy(spacing.md),
            ) {
                AllergensSection(allergens = product.allergens)
                ProductField(
                    label = stringResource(R.string.product_field_ingredients),
                    value = product.ingredients,
                    isHeading = true,
                )
                Text(
                    text = stringResource(R.string.product_section_details),
                    style = MaterialTheme.typography.headlineSmall,
                    modifier = Modifier.semantics { heading() },
                )
                ProductField(
                    label = stringResource(R.string.product_field_name),
                    value = product.name,
                )
                ProductField(
                    label = stringResource(R.string.product_field_brand),
                    value = product.brand,
                )
                Column(verticalArrangement = Arrangement.spacedBy(spacing.sm)) {
                    Text(
                        text = stringResource(R.string.product_field_nutriscore),
                        style = MaterialTheme.typography.labelLarge,
                    )
                    NutriScoreBadge(nutriScore = product.nutriScore)
                }
                Text(
                    text = stringResource(R.string.product_field_code, product.code),
                    style = MaterialTheme.typography.bodyMedium,
                )
            }
        }
        Text(
            text = stringResource(R.string.product_disclaimer),
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        PrimaryActionButton(
            text = stringResource(R.string.action_new_search),
            onClick = onNewSearch,
        )
    }
}

@Composable
private fun ProductField(
    label: String,
    value: String?,
    isHeading: Boolean = false,
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(AlertaRotuloThemeExtras.spacing.xs),
    ) {
        Text(
            text = label,
            style = if (isHeading) {
                MaterialTheme.typography.headlineSmall
            } else {
                MaterialTheme.typography.labelLarge
            },
            modifier = if (isHeading) {
                Modifier.semantics { heading() }
            } else {
                Modifier
            },
        )
        Text(
            text = value ?: stringResource(R.string.product_field_not_informed),
            style = MaterialTheme.typography.bodyLarge,
        )
    }
}
