package com.amdcruz.alertarotulo.core.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val LightColorScheme = lightColorScheme(
    primary = Primary,
    onPrimary = OnPrimary,
    primaryContainer = PrimaryContainer,
    onPrimaryContainer = OnPrimaryContainer,
    secondary = Secondary,
    onSecondary = OnSecondary,
    secondaryContainer = SecondaryContainer,
    onSecondaryContainer = OnSecondaryContainer,
    tertiary = Tertiary,
    onTertiary = OnTertiary,
    tertiaryContainer = TertiaryContainer,
    onTertiaryContainer = OnTertiaryContainer,
    error = Error,
    onError = OnError,
    errorContainer = ErrorContainer,
    onErrorContainer = OnErrorContainer,
    background = Background,
    onBackground = OnBackground,
    surface = Surface,
    onSurface = OnSurface,
    surfaceVariant = SurfaceVariant,
    onSurfaceVariant = OnSurfaceVariant,
    outline = Outline,
    outlineVariant = OutlineVariant,
    inverseSurface = InverseSurface,
    inverseOnSurface = InverseOnSurface,
    inversePrimary = InversePrimary,
)

private val DarkColorScheme = darkColorScheme(
    primary = InversePrimary,
    onPrimary = OnPrimaryContainer,
    primaryContainer = Primary,
    onPrimaryContainer = PrimaryContainer,
    secondary = SecondaryContainer,
    onSecondary = OnSecondaryContainer,
    secondaryContainer = Secondary,
    onSecondaryContainer = SecondaryContainer,
    tertiary = TertiaryContainer,
    onTertiary = OnTertiaryContainer,
    tertiaryContainer = Tertiary,
    onTertiaryContainer = TertiaryContainer,
    error = ErrorContainer,
    onError = OnErrorContainer,
    errorContainer = Error,
    onErrorContainer = ErrorContainer,
    background = InverseSurface,
    onBackground = InverseOnSurface,
    surface = InverseSurface,
    onSurface = InverseOnSurface,
    surfaceVariant = OnSurfaceVariant,
    onSurfaceVariant = SurfaceVariant,
    outline = OutlineVariant,
    outlineVariant = Outline,
    inverseSurface = Surface,
    inverseOnSurface = OnSurface,
    inversePrimary = Primary,
)

@Composable
fun AlertaRotuloTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit,
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme
    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content,
    )
}
