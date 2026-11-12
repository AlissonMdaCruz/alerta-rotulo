package com.amdcruz.alertarotulo.navigation

import androidx.compose.runtime.Composable
import androidx.lifecycle.compose.dropUnlessResumed
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.runtime.rememberSaveableStateHolderNavEntryDecorator
import androidx.navigation3.ui.NavDisplay
import com.amdcruz.alertarotulo.history.ui.HistoryRoute
import com.amdcruz.alertarotulo.product.ui.ProductLookupRoute
import com.amdcruz.alertarotulo.scan.ui.ManualEntryScreen
import com.amdcruz.alertarotulo.scan.ui.ScanCaptureRoute

@Composable
fun AlertaRotuloNavDisplay() {
    val backStack = rememberNavBackStack(ScanRoute)

    fun popToScan() {
        while (backStack.size > 1) {
            backStack.removeLastOrNull()
        }
    }

    NavDisplay(
        backStack = backStack,
        onBack = { backStack.removeLastOrNull() },
        entryDecorators = listOf(
            rememberSaveableStateHolderNavEntryDecorator(),
            rememberViewModelStoreNavEntryDecorator(),
        ),
        entryProvider = entryProvider {
            entry<ScanRoute> {
                ScanCaptureRoute(
                    onBarcodeValidated = { code ->
                        backStack.add(ProductRoute(code = code))
                    },
                    onOpenManualEntry = dropUnlessResumed {
                        backStack.add(ManualEntryRoute)
                    },
                    onOpenHistory = dropUnlessResumed {
                        backStack.add(HistoryRoute)
                    },
                )
            }
            entry<ManualEntryRoute> {
                ManualEntryScreen(
                    onSubmitValidCode = { code ->
                        backStack.add(ProductRoute(code = code))
                    },
                    onBack = { backStack.removeLastOrNull() },
                )
            }
            entry<ProductRoute> { key ->
                ProductLookupRoute(
                    code = key.code,
                    fromHistory = key.fromHistory,
                    onBack = { backStack.removeLastOrNull() },
                    onNewSearch = { popToScan() },
                )
            }
            entry<HistoryRoute> {
                HistoryRoute(
                    onItemClick = { code ->
                        backStack.add(ProductRoute(code = code, fromHistory = true))
                    },
                    onStartNewSearch = { popToScan() },
                    onBack = { backStack.removeLastOrNull() },
                )
            }
        },
    )
}
