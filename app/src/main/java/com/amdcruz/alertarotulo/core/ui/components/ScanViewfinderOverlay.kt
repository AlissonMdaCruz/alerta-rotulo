package com.amdcruz.alertarotulo.core.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.RoundRect
import androidx.compose.ui.graphics.ClipOp
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.clipPath
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import com.amdcruz.alertarotulo.core.ui.theme.ScrimDark
import com.amdcruz.alertarotulo.core.ui.theme.ViewfinderAccent

@Composable
fun ScanViewfinderOverlay(
    modifier: Modifier = Modifier,
) {
    val cornerLength = with(LocalDensity.current) { 28.dp.toPx() }
    val strokeWidth = with(LocalDensity.current) { 4.dp.toPx() }
    val frameCornerRadius = with(LocalDensity.current) { 16.dp.toPx() }

    Canvas(modifier = modifier.fillMaxSize()) {
        val frameWidth = size.width * 0.72f
        val frameHeight = frameWidth * 0.55f
        val left = (size.width - frameWidth) / 2f
        val top = size.height * 0.28f
        val frameRect = Rect(
            offset = Offset(left, top),
            size = androidx.compose.ui.geometry.Size(frameWidth, frameHeight),
        )
        val cutoutPath = Path().apply {
            addRoundRect(
                RoundRect(
                    rect = frameRect,
                    cornerRadius = CornerRadius(frameCornerRadius, frameCornerRadius),
                ),
            )
        }
        clipPath(cutoutPath, clipOp = ClipOp.Difference) {
            drawRect(color = ScrimDark)
        }

        val accent = ViewfinderAccent
        // Top-left
        drawLine(
            color = accent,
            start = Offset(frameRect.left, frameRect.top + cornerLength),
            end = Offset(frameRect.left, frameRect.top),
            strokeWidth = strokeWidth,
            cap = StrokeCap.Round,
        )
        drawLine(
            color = accent,
            start = Offset(frameRect.left, frameRect.top),
            end = Offset(frameRect.left + cornerLength, frameRect.top),
            strokeWidth = strokeWidth,
            cap = StrokeCap.Round,
        )
        // Top-right
        drawLine(
            color = accent,
            start = Offset(frameRect.right - cornerLength, frameRect.top),
            end = Offset(frameRect.right, frameRect.top),
            strokeWidth = strokeWidth,
            cap = StrokeCap.Round,
        )
        drawLine(
            color = accent,
            start = Offset(frameRect.right, frameRect.top),
            end = Offset(frameRect.right, frameRect.top + cornerLength),
            strokeWidth = strokeWidth,
            cap = StrokeCap.Round,
        )
        // Bottom-left
        drawLine(
            color = accent,
            start = Offset(frameRect.left, frameRect.bottom - cornerLength),
            end = Offset(frameRect.left, frameRect.bottom),
            strokeWidth = strokeWidth,
            cap = StrokeCap.Round,
        )
        drawLine(
            color = accent,
            start = Offset(frameRect.left, frameRect.bottom),
            end = Offset(frameRect.left + cornerLength, frameRect.bottom),
            strokeWidth = strokeWidth,
            cap = StrokeCap.Round,
        )
        // Bottom-right
        drawLine(
            color = accent,
            start = Offset(frameRect.right - cornerLength, frameRect.bottom),
            end = Offset(frameRect.right, frameRect.bottom),
            strokeWidth = strokeWidth,
            cap = StrokeCap.Round,
        )
        drawLine(
            color = accent,
            start = Offset(frameRect.right, frameRect.bottom - cornerLength),
            end = Offset(frameRect.right, frameRect.bottom),
            strokeWidth = strokeWidth,
            cap = StrokeCap.Round,
        )

        drawRoundRect(
            color = accent.copy(alpha = 0.35f),
            topLeft = frameRect.topLeft,
            size = frameRect.size,
            cornerRadius = CornerRadius(frameCornerRadius, frameCornerRadius),
            style = Stroke(width = 1.dp.toPx()),
        )
    }
}
