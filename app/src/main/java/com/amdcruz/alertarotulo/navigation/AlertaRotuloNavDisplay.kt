package com.amdcruz.alertarotulo.navigation

import androidx.compose.runtime.Composable
import androidx.lifecycle.compose.dropUnlessResumed
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.runtime.rememberSaveableStateHolderNavEntryDecorator
import androidx.navigation3.ui.NavDisplay
import com.amdcruz.alertarotulo.history.ui.HistoryListPlaceholderScreen
import com.amdcruz.alertarotulo.product.ui.ProductLookupRoute
import com.amdcruz.alertarotulo.scan.ui.ManualEntryPlaceholderScreen
import com.amdcruz.alertarotulo.scan.ui.ScanCapturePlaceholderScreen

private const val SAMPLE_PRODUCT_CODE: String = "3017620422003"

@Composable
fun AlertaRotuloNavDisplay() {
    val backStack = rememberNavBackStack(ScanRoute)

    NavDisplay(
        backStack = backStack,
        onBack = { backStack.removeLastOrNull() },
        entryDecorators = listOf(
            rememberSaveableStateHolderNavEntryDecorator(),
            rememberViewModelStoreNavEntryDecorator(),
        ),
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
                        backStack.add(ProductRoute(code = SAMPLE_PRODUCT_CODE))
                    },
                )
            }
            entry<ManualEntryRoute> {
                ManualEntryPlaceholderScreen(
                    onSubmitCode = { code ->
                        backStack.add(ProductRoute(code = code))
                    },
                    onBack = { backStack.removeLastOrNull() },
                )
            }
            entry<ProductRoute> { key ->
                ProductLookupRoute(
                    code = key.code,
                    onBack = { backStack.removeLastOrNull() },
                    onNewSearch = {
                        while (backStack.size > 1) {
                            backStack.removeLastOrNull()
                        }
                    },
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
