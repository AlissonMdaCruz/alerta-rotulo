package com.amdcruz.alertarotulo.core.ui.theme

import androidx.compose.runtime.Immutable
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Immutable
data class Spacing(
    val xs: Dp = 4.dp,
    val sm: Dp = 8.dp,
    val md: Dp = 16.dp,
    val lg: Dp = 24.dp,
    val xl: Dp = 32.dp,
    val screenMargin: Dp = 16.dp,
    val screenMarginWide: Dp = 24.dp,
    val minTouchTarget: Dp = 48.dp,
    val minButtonHeight: Dp = 52.dp,
    val statusStripHeight: Dp = 8.dp,
)

val AppSpacing = Spacing()
