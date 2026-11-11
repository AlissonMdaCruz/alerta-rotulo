package com.amdcruz.alertarotulo.product.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
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
    Scaffold(modifier = modifier.fillMaxSize()) { innerPadding ->
        when (uiState) {
            ProductUiState.Idle,
            ProductUiState.Loading,
            -> LoadingContent(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
            )

            is ProductUiState.Success -> SuccessContent(
                product = uiState.product,
                isFromHistory = isFromHistory,
                onNewSearch = onNewSearch,
                onBack = onBack,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
            )

            is ProductUiState.NotFound -> NotFoundContent(
                code = uiState.code,
                onNewSearch = onNewSearch,
                onBack = onBack,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
            )

            is ProductUiState.NetworkError -> NetworkErrorContent(
                code = uiState.code,
                onRetry = onRetry,
                onNewSearch = onNewSearch,
                onBack = onBack,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
            )
        }
    }
}

@Composable
private fun LoadingContent(modifier: Modifier = Modifier) {
    val loadingLabel = stringResource(R.string.product_loading)
    Column(
        modifier = modifier
            .padding(24.dp)
            .semantics {
                liveRegion = LiveRegionMode.Polite
                contentDescription = loadingLabel
            },
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        CircularProgressIndicator()
        Text(
            text = loadingLabel,
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.padding(top = 16.dp),
        )
    }
}

@Composable
private fun SuccessContent(
    product: Product,
    isFromHistory: Boolean,
    onNewSearch: () -> Unit,
    onBack: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .verticalScroll(rememberScrollState())
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp),
    ) {
        Text(
            text = stringResource(R.string.product_result_title),
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.semantics { heading() },
        )
        if (isFromHistory) {
            Text(
                text = stringResource(R.string.product_saved_lookup_label),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
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
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
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
        Text(
            text = stringResource(R.string.product_disclaimer),
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        PrimaryActionButton(
            text = stringResource(R.string.action_new_search),
            onClick = onNewSearch,
        )
        SecondaryActionButton(
            text = stringResource(R.string.action_back),
            onClick = onBack,
        )
    }
}

@Composable
private fun NotFoundContent(
    code: String,
    onNewSearch: () -> Unit,
    onBack: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val title = stringResource(R.string.product_not_found_title)
    val body = stringResource(R.string.product_not_found_body, code)
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
            text = stringResource(R.string.action_new_search),
            onClick = onNewSearch,
        )
        SecondaryActionButton(
            text = stringResource(R.string.action_back),
            onClick = onBack,
        )
    }
}

@Composable
private fun NetworkErrorContent(
    code: String,
    onRetry: () -> Unit,
    onNewSearch: () -> Unit,
    onBack: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val title = stringResource(R.string.product_network_error_title)
    val body = stringResource(R.string.product_network_error_body, code)
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
            text = stringResource(R.string.action_retry),
            onClick = onRetry,
        )
        SecondaryActionButton(
            text = stringResource(R.string.action_new_search),
            onClick = onNewSearch,
        )
        SecondaryActionButton(
            text = stringResource(R.string.action_back),
            onClick = onBack,
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
        verticalArrangement = Arrangement.spacedBy(4.dp),
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
