package com.amdcruz.alertarotulo.history.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.heading
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import com.amdcruz.alertarotulo.R
import com.amdcruz.alertarotulo.core.ui.components.PrimaryActionButton
import com.amdcruz.alertarotulo.core.ui.components.SecondaryActionButton
import com.amdcruz.alertarotulo.history.data.model.HistoryItem
import java.text.DateFormat
import java.util.Date

@Composable
fun HistoryListScreen(
    uiState: HistoryUiState,
    onItemClick: (String) -> Unit,
    onStartNewSearch: () -> Unit,
    onBack: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Scaffold(modifier = modifier.fillMaxSize()) { innerPadding ->
        if (uiState.items.isEmpty()) {
            EmptyHistoryContent(
                onStartNewSearch = onStartNewSearch,
                onBack = onBack,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
            )
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
            ) {
                Text(
                    text = stringResource(R.string.history_title),
                    style = MaterialTheme.typography.headlineMedium,
                    modifier = Modifier
                        .padding(horizontal = 24.dp, vertical = 16.dp)
                        .semantics { heading() },
                )
                LazyColumn(
                    modifier = Modifier.weight(1f),
                    contentPadding = PaddingValues(horizontal = 24.dp, vertical = 8.dp),
                    verticalArrangement = Arrangement.spacedBy(4.dp),
                ) {
                    items(
                        items = uiState.items,
                        key = { item -> item.code },
                    ) { item ->
                        HistoryListRow(
                            item = item,
                            onClick = { onItemClick(item.code) },
                        )
                        HorizontalDivider()
                    }
                }
                SecondaryActionButton(
                    text = stringResource(R.string.action_back),
                    onClick = onBack,
                    modifier = Modifier.padding(24.dp),
                )
            }
        }
    }
}

@Composable
private fun EmptyHistoryContent(
    onStartNewSearch: () -> Unit,
    onBack: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier.padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        Text(
            text = stringResource(R.string.history_title),
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.semantics { heading() },
        )
        Text(
            text = stringResource(R.string.history_empty_body),
            style = MaterialTheme.typography.bodyLarge,
        )
        PrimaryActionButton(
            text = stringResource(R.string.action_new_search),
            onClick = onStartNewSearch,
        )
        SecondaryActionButton(
            text = stringResource(R.string.action_back),
            onClick = onBack,
        )
    }
}

@Composable
private fun HistoryListRow(
    item: HistoryItem,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val title = item.name?.takeIf { it.isNotBlank() }
        ?: stringResource(R.string.history_item_unnamed)
    val subtitle = buildString {
        append(item.code)
        val brand = item.brand?.takeIf { it.isNotBlank() }
        if (brand != null) {
            append(" · ")
            append(brand)
        }
    }
    val consultedAt = DateFormat.getDateTimeInstance(
        DateFormat.MEDIUM,
        DateFormat.SHORT,
    ).format(Date(item.consultedAtEpochMillis))

    Column(
        modifier = modifier
            .fillMaxWidth()
            .heightIn(min = 64.dp)
            .clickable(onClick = onClick)
            .padding(vertical = 12.dp),
        verticalArrangement = Arrangement.spacedBy(4.dp),
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleMedium,
        )
        Text(
            text = subtitle,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        Text(
            text = stringResource(R.string.history_item_consulted_at, consultedAt),
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
    }
}
