package com.amdcruz.alertarotulo.product.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.amdcruz.alertarotulo.R
import com.amdcruz.alertarotulo.product.data.model.Product

@Composable
fun ProductScreen(
    uiState: ProductUiState,
    onRetry: () -> Unit,
    onNewSearch: () -> Unit,
    onBack: () -> Unit,
    modifier: Modifier = Modifier,
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
    Column(
        modifier = modifier.padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        CircularProgressIndicator()
        Text(
            text = stringResource(R.string.product_loading),
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.padding(top = 16.dp),
        )
    }
}

@Composable
private fun SuccessContent(
    product: Product,
    onNewSearch: () -> Unit,
    onBack: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .verticalScroll(rememberScrollState())
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        Text(
            text = stringResource(R.string.product_result_title),
            style = MaterialTheme.typography.headlineMedium,
        )
        ProductField(
            label = stringResource(R.string.product_field_allergens),
            value = product.allergens,
        )
        ProductField(
            label = stringResource(R.string.product_field_ingredients),
            value = product.ingredients,
        )
        ProductField(
            label = stringResource(R.string.product_field_name),
            value = product.name,
        )
        ProductField(
            label = stringResource(R.string.product_field_brand),
            value = product.brand,
        )
        ProductField(
            label = stringResource(R.string.product_field_nutriscore),
            value = product.nutriScore?.toString(),
        )
        Text(
            text = stringResource(R.string.product_field_code, product.code),
            style = MaterialTheme.typography.bodyMedium,
        )
        Button(
            onClick = onNewSearch,
            modifier = Modifier.fillMaxWidth(),
        ) {
            Text(stringResource(R.string.action_new_search))
        }
        OutlinedButton(
            onClick = onBack,
            modifier = Modifier.fillMaxWidth(),
        ) {
            Text(stringResource(R.string.action_back))
        }
    }
}

@Composable
private fun NotFoundContent(
    code: String,
    onNewSearch: () -> Unit,
    onBack: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier.padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        Text(
            text = stringResource(R.string.product_not_found_title),
            style = MaterialTheme.typography.headlineMedium,
        )
        Text(
            text = stringResource(R.string.product_not_found_body, code),
            style = MaterialTheme.typography.bodyLarge,
        )
        Button(
            onClick = onNewSearch,
            modifier = Modifier.fillMaxWidth(),
        ) {
            Text(stringResource(R.string.action_new_search))
        }
        OutlinedButton(
            onClick = onBack,
            modifier = Modifier.fillMaxWidth(),
        ) {
            Text(stringResource(R.string.action_back))
        }
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
    Column(
        modifier = modifier.padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        Text(
            text = stringResource(R.string.product_network_error_title),
            style = MaterialTheme.typography.headlineMedium,
        )
        Text(
            text = stringResource(R.string.product_network_error_body, code),
            style = MaterialTheme.typography.bodyLarge,
        )
        Button(
            onClick = onRetry,
            modifier = Modifier.fillMaxWidth(),
        ) {
            Text(stringResource(R.string.action_retry))
        }
        OutlinedButton(
            onClick = onNewSearch,
            modifier = Modifier.fillMaxWidth(),
        ) {
            Text(stringResource(R.string.action_new_search))
        }
        OutlinedButton(
            onClick = onBack,
            modifier = Modifier.fillMaxWidth(),
        ) {
            Text(stringResource(R.string.action_back))
        }
    }
}

@Composable
private fun ProductField(
    label: String,
    value: String?,
) {
    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelLarge,
        )
        Text(
            text = value ?: stringResource(R.string.product_field_not_informed),
            style = MaterialTheme.typography.bodyLarge,
        )
    }
}
