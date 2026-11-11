package com.amdcruz.alertarotulo.navigation

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

@Serializable
data object ScanRoute : NavKey

@Serializable
data object ManualEntryRoute : NavKey

@Serializable
data class ProductRoute(val code: String) : NavKey

@Serializable
data object HistoryRoute : NavKey
