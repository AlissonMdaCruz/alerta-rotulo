package com.amdcruz.alertarotulo.scan.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
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
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.amdcruz.alertarotulo.R
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

    fun submit() {
        val validated = BarcodeValidator.validate(codeInput)
        if (validated == null) {
            showValidationError = true
            return
        }
        showValidationError = false
        onSubmitValidCode(validated)
    }

    Scaffold(modifier = modifier.fillMaxSize()) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            Text(
                text = stringResource(R.string.manual_entry_title),
                style = MaterialTheme.typography.headlineMedium,
            )
            Text(
                text = stringResource(R.string.manual_entry_body),
                style = MaterialTheme.typography.bodyLarge,
            )
            OutlinedTextField(
                value = codeInput,
                onValueChange = { value ->
                    codeInput = BarcodeValidator.normalize(value)
                    showValidationError = false
                },
                modifier = Modifier.fillMaxWidth(),
                label = { Text(stringResource(R.string.manual_entry_code_label)) },
                supportingText = {
                    if (showValidationError) {
                        Text(stringResource(R.string.manual_entry_invalid_code))
                    } else {
                        Text(stringResource(R.string.manual_entry_formats_hint))
                    }
                },
                isError = showValidationError,
                singleLine = true,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number,
                    imeAction = ImeAction.Done,
                ),
                keyboardActions = KeyboardActions(onDone = { submit() }),
            )
            Button(
                onClick = { submit() },
                enabled = normalizedCode.isNotEmpty(),
                modifier = Modifier.fillMaxWidth(),
            ) {
                Text(stringResource(R.string.action_lookup_product))
            }
            OutlinedButton(
                onClick = onBack,
                modifier = Modifier.fillMaxWidth(),
            ) {
                Text(stringResource(R.string.action_back))
            }
        }
    }
}
