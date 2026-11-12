package com.amdcruz.alertarotulo.scan.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.LiveRegionMode
import androidx.compose.ui.semantics.error
import androidx.compose.ui.semantics.liveRegion
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.amdcruz.alertarotulo.R
import com.amdcruz.alertarotulo.core.ui.components.AppTopBar
import com.amdcruz.alertarotulo.core.ui.components.PrimaryActionButton
import com.amdcruz.alertarotulo.core.ui.components.ProductInfoCard
import com.amdcruz.alertarotulo.core.ui.theme.AlertaRotuloThemeExtras
import com.amdcruz.alertarotulo.scan.BarcodeValidator

@Composable
fun ManualEntryScreen(
    onSubmitValidCode: (String) -> Unit,
    onBack: () -> Unit,
    modifier: Modifier = Modifier,
) {
    var codeInput by rememberSaveable { mutableStateOf("") }
    var showValidationError by rememberSaveable { mutableStateOf(false) }
    val normalizedCode = BarcodeValidator.normalize(codeInput)
    val invalidMessage = stringResource(R.string.manual_entry_invalid_code)
    val spacing = AlertaRotuloThemeExtras.spacing

    fun submit() {
        val validated = BarcodeValidator.validate(codeInput)
        if (validated == null) {
            showValidationError = true
            return
        }
        showValidationError = false
        onSubmitValidCode(validated)
    }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            AppTopBar(
                title = stringResource(R.string.manual_entry_title),
                onNavigateBack = onBack,
            )
        },
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .padding(spacing.screenMarginWide),
            verticalArrangement = Arrangement.spacedBy(spacing.md),
        ) {
            Text(
                text = stringResource(R.string.manual_entry_body),
                style = MaterialTheme.typography.bodyLarge,
            )
            ProductInfoCard {
                Column(
                    modifier = Modifier.padding(spacing.md),
                    verticalArrangement = Arrangement.spacedBy(spacing.sm),
                ) {
                    Text(
                        text = stringResource(R.string.manual_entry_code_label),
                        style = MaterialTheme.typography.labelLarge,
                    )
                    OutlinedTextField(
                        value = codeInput,
                        onValueChange = { value ->
                            codeInput = BarcodeValidator.normalize(value)
                            showValidationError = false
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .heightIn(min = 52.dp)
                            .then(
                                if (showValidationError) {
                                    Modifier.semantics {
                                        error(invalidMessage)
                                        liveRegion = LiveRegionMode.Assertive
                                    }
                                } else {
                                    Modifier
                                },
                            ),
                        supportingText = {
                            if (showValidationError) {
                                Text(invalidMessage)
                            } else {
                                Text(stringResource(R.string.manual_entry_formats_hint))
                            }
                        },
                        isError = showValidationError,
                        singleLine = true,
                        shape = MaterialTheme.shapes.medium,
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Number,
                            imeAction = ImeAction.Done,
                        ),
                        keyboardActions = KeyboardActions(onDone = { submit() }),
                    )
                }
            }
            PrimaryActionButton(
                text = stringResource(R.string.action_lookup_product),
                onClick = { submit() },
                enabled = normalizedCode.isNotEmpty(),
            )
        }
    }
}
