package com.amdcruz.alertarotulo.navigation

import androidx.compose.runtime.Composable
import androidx.lifecycle.compose.dropUnlessResumed
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.ui.NavDisplay
import com.amdcruz.alertarotulo.history.ui.HistoryListPlaceholderScreen
import com.amdcruz.alertarotulo.product.ui.ProductResultPlaceholderScreen
import com.amdcruz.alertarotulo.scan.ui.ManualEntryPlaceholderScreen
import com.amdcruz.alertarotulo.scan.ui.ScanCapturePlaceholderScreen

@Composable
fun AlertaRotuloNavDisplay() {
    val backStack = rememberNavBackStack(ScanRoute)

    NavDisplay(
        backStack = backStack,
        onBack = { backStack.removeLastOrNull() },
        entryProvider = entryProvider {
            entry<ScanRoute> {
                ScanCapturePlaceholderScreen(
                    onOpenManualEntry = dropUnlessResumed {
                        backStack.add(ManualEntryRoute)
                    },
                    onOpenHistory = dropUnlessResumed {
                        backStack.add(HistoryRoute)
                    },
                    onOpenSampleProduct = dropUnlessResumed {
                        backStack.add(ProductRoute(code = "0000000000000"))
                    },
                )
            }
            entry<ManualEntryRoute> {
                ManualEntryPlaceholderScreen(
                    onBack = { backStack.removeLastOrNull() },
                )
            }
            entry<ProductRoute> { key ->
                ProductResultPlaceholderScreen(
                    code = key.code,
                    onBack = { backStack.removeLastOrNull() },
                )
            }
            entry<HistoryRoute> {
                HistoryListPlaceholderScreen(
                    onBack = { backStack.removeLastOrNull() },
                )
            }
        },
    )
}
