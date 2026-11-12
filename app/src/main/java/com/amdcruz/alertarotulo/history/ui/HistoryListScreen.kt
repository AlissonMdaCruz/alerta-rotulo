package com.amdcruz.alertarotulo.history.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ListAlt
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import com.amdcruz.alertarotulo.R
import com.amdcruz.alertarotulo.core.ui.components.AppTopBar
import com.amdcruz.alertarotulo.core.ui.components.NutriScoreMiniLabel
import com.amdcruz.alertarotulo.core.ui.components.PrimaryActionButton
import com.amdcruz.alertarotulo.core.ui.components.StatusStateContent
import com.amdcruz.alertarotulo.core.ui.theme.AlertaRotuloThemeExtras
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
    val spacing = AlertaRotuloThemeExtras.spacing

    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            AppTopBar(
                title = stringResource(R.string.history_title),
                onNavigateBack = onBack,
            )
        },
    ) { innerPadding ->
        if (uiState.items.isEmpty()) {
            StatusStateContent(
                title = stringResource(R.string.history_empty_title),
                body = stringResource(R.string.history_empty_body),
                icon = Icons.AutoMirrored.Filled.ListAlt,
                assertiveLiveRegion = false,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
            ) {
                PrimaryActionButton(
                    text = stringResource(R.string.action_new_search),
                    onClick = onStartNewSearch,
                )
            }
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
            ) {
                LazyColumn(
                    modifier = Modifier.weight(1f),
                    contentPadding = PaddingValues(
                        horizontal = spacing.screenMarginWide,
                        vertical = spacing.sm,
                    ),
                    verticalArrangement = Arrangement.spacedBy(spacing.sm),
                ) {
                    items(
                        items = uiState.items,
                        key = { item -> item.code },
                    ) { item ->
                        HistoryListRow(
                            item = item,
                            onClick = { onItemClick(item.code) },
                        )
                    }
                }
                PrimaryActionButton(
                    text = stringResource(R.string.action_new_search),
                    onClick = onStartNewSearch,
                    modifier = Modifier.padding(spacing.screenMarginWide),
                )
            }
        }
    }
}

@Composable
private fun HistoryListRow(
    item: HistoryItem,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val spacing = AlertaRotuloThemeExtras.spacing
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
    val consultedLabel = stringResource(R.string.history_item_consulted_at, consultedAt)
    val extras = buildList {
        if (!item.allergens.isNullOrBlank()) {
            add(stringResource(R.string.history_item_has_allergens))
        }
        item.nutriScore?.let { grade ->
            add(stringResource(R.string.history_item_nutriscore, grade.uppercaseChar()))
        }
    }.joinToString(". ")
    val a11yDescription = stringResource(
        R.string.history_item_a11y,
        title,
        subtitle,
        buildString {
            append(consultedLabel)
            if (extras.isNotEmpty()) {
                append(". ")
                append(extras)
            }
        },
    )

    Surface(
        onClick = onClick,
        modifier = modifier
            .fillMaxWidth()
            .heightIn(min = 64.dp)
            .semantics {
                contentDescription = a11yDescription
            },
        shape = MaterialTheme.shapes.medium,
        color = MaterialTheme.colorScheme.surfaceContainerLowest,
        border = BorderStroke(1.5.dp, MaterialTheme.colorScheme.outlineVariant),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(spacing.md)
                .clearAndSetSemantics { },
            horizontalArrangement = Arrangement.spacedBy(spacing.md),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(spacing.xs),
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
                    text = consultedLabel,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
                if (!item.allergens.isNullOrBlank()) {
                    Text(
                        text = stringResource(R.string.history_item_has_allergens),
                        style = MaterialTheme.typography.labelMedium,
                        color = AlertaRotuloThemeExtras.semanticColors.critical.onContainer,
                    )
                }
            }
            item.nutriScore?.let { grade ->
                NutriScoreMiniLabel(grade = grade)
            }
        }
    }
}
