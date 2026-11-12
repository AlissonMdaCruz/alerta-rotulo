package com.amdcruz.alertarotulo.core.ui.theme

import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.Color

val Primary = Color(0xFF006C49)
val OnPrimary = Color(0xFFFFFFFF)
val PrimaryContainer = Color(0xFF10B981)
val OnPrimaryContainer = Color(0xFF00422B)

val Secondary = Color(0xFF855300)
val OnSecondary = Color(0xFFFFFFFF)
val SecondaryContainer = Color(0xFFFEA619)
val OnSecondaryContainer = Color(0xFF684000)

val Tertiary = Color(0xFFB91A24)
val OnTertiary = Color(0xFFFFFFFF)
val TertiaryContainer = Color(0xFFFF7A73)
val OnTertiaryContainer = Color(0xFF79000E)

val Error = Color(0xFFBA1A1A)
val OnError = Color(0xFFFFFFFF)
val ErrorContainer = Color(0xFFFFDAD6)
val OnErrorContainer = Color(0xFF93000A)

val Background = Color(0xFFFAF8FF)
val OnBackground = Color(0xFF131B2E)
val Surface = Color(0xFFFAF8FF)
val OnSurface = Color(0xFF131B2E)
val SurfaceVariant = Color(0xFFDAE2FD)
val OnSurfaceVariant = Color(0xFF3C4A42)
val Outline = Color(0xFF6C7A71)
val OutlineVariant = Color(0xFFBBCABF)

val InverseSurface = Color(0xFF283044)
val InverseOnSurface = Color(0xFFEEF0FF)
val InversePrimary = Color(0xFF4EDEA3)

val SurfaceDim = Color(0xFFD2D9F4)
val SurfaceBright = Color(0xFFFAF8FF)
val SurfaceContainerLowest = Color(0xFFFFFFFF)
val SurfaceContainerLow = Color(0xFFF2F3FF)
val SurfaceContainer = Color(0xFFEAEDFF)
val SurfaceContainerHigh = Color(0xFFE2E7FF)
val SurfaceContainerHighest = Color(0xFFDAE2FD)

val CardBorder = Color(0xFFE2E8F0)
val FocusRing = Color(0xFF0F172A)
val ViewfinderAccent = Color(0xFF10B981)
val ScrimDark = Color(0x99000000)

val DarkBackground = Color(0xFF0F1419)
val DarkSurface = Color(0xFF0F1419)
val DarkOnSurface = Color(0xFFE8EAED)
val DarkSurfaceVariant = Color(0xFF2A3340)
val DarkOnSurfaceVariant = Color(0xFFC4CDD6)
val DarkOutline = Color(0xFF8A939C)
val DarkOutlineVariant = Color(0xFF3F4852)
val DarkSurfaceContainerLowest = Color(0xFF1A2129)
val DarkSurfaceContainer = Color(0xFF1E262E)
val DarkSurfaceContainerHigh = Color(0xFF282F38)
val DarkError = Color(0xFFFFB4AB)
val DarkOnError = Color(0xFF690005)
val DarkErrorContainer = Color(0xFF93000A)
val DarkOnErrorContainer = Color(0xFFFFDAD6)

@Immutable
data class SemanticColorPair(
    val container: Color,
    val onContainer: Color,
    val border: Color,
)

@Immutable
data class SemanticColors(
    val safe: SemanticColorPair,
    val caution: SemanticColorPair,
    val critical: SemanticColorPair,
)

val LightSemanticColors = SemanticColors(
    safe = SemanticColorPair(
        container = Color(0xFFECFDF5),
        onContainer = Color(0xFF065F46),
        border = Color(0xFF065F46),
    ),
    caution = SemanticColorPair(
        container = Color(0xFFFFFBEB),
        onContainer = Color(0xFF92400E),
        border = Color(0xFF92400E),
    ),
    critical = SemanticColorPair(
        container = Color(0xFFFEF2F2),
        onContainer = Color(0xFF991B1B),
        border = Color(0xFF991B1B),
    ),
)

val DarkSemanticColors = SemanticColors(
    safe = SemanticColorPair(
        container = Color(0xFF064E3B),
        onContainer = Color(0xFFA7F3D0),
        border = Color(0xFF6EE7B7),
    ),
    caution = SemanticColorPair(
        container = Color(0xFF78350F),
        onContainer = Color(0xFFFDE68A),
        border = Color(0xFFFBBF24),
    ),
    critical = SemanticColorPair(
        container = Color(0xFF7F1D1D),
        onContainer = Color(0xFFFECACA),
        border = Color(0xFFF87171),
    ),
)

@Immutable
data class NutriScoreColors(
    val gradeA: SemanticColorPair,
    val gradeB: SemanticColorPair,
    val gradeC: SemanticColorPair,
    val gradeD: SemanticColorPair,
    val gradeE: SemanticColorPair,
    val unknown: SemanticColorPair,
)

val LightNutriScoreColors = NutriScoreColors(
    gradeA = SemanticColorPair(
        container = Color(0xFFECFDF5),
        onContainer = Color(0xFF065F46),
        border = Color(0xFF059669),
    ),
    gradeB = SemanticColorPair(
        container = Color(0xFFF0FDF4),
        onContainer = Color(0xFF166534),
        border = Color(0xFF22C55E),
    ),
    gradeC = SemanticColorPair(
        container = Color(0xFFFFFBEB),
        onContainer = Color(0xFF92400E),
        border = Color(0xFFF59E0B),
    ),
    gradeD = SemanticColorPair(
        container = Color(0xFFFFF7ED),
        onContainer = Color(0xFF9A3412),
        border = Color(0xFFEA580C),
    ),
    gradeE = SemanticColorPair(
        container = Color(0xFFFEF2F2),
        onContainer = Color(0xFF991B1B),
        border = Color(0xFFEF4444),
    ),
    unknown = SemanticColorPair(
        container = Color(0xFFF1F5F9),
        onContainer = Color(0xFF334155),
        border = Color(0xFF94A3B8),
    ),
)

val DarkNutriScoreColors = NutriScoreColors(
    gradeA = SemanticColorPair(
        container = Color(0xFF064E3B),
        onContainer = Color(0xFFA7F3D0),
        border = Color(0xFF34D399),
    ),
    gradeB = SemanticColorPair(
        container = Color(0xFF14532D),
        onContainer = Color(0xFFBBF7D0),
        border = Color(0xFF4ADE80),
    ),
    gradeC = SemanticColorPair(
        container = Color(0xFF78350F),
        onContainer = Color(0xFFFDE68A),
        border = Color(0xFFFBBF24),
    ),
    gradeD = SemanticColorPair(
        container = Color(0xFF7C2D12),
        onContainer = Color(0xFFFED7AA),
        border = Color(0xFFFB923C),
    ),
    gradeE = SemanticColorPair(
        container = Color(0xFF7F1D1D),
        onContainer = Color(0xFFFECACA),
        border = Color(0xFFF87171),
    ),
    unknown = SemanticColorPair(
        container = Color(0xFF1E293B),
        onContainer = Color(0xFFCBD5E1),
        border = Color(0xFF64748B),
    ),
)

fun NutriScoreColors.forGrade(grade: Char?): SemanticColorPair {
    return when (grade?.uppercaseChar()) {
        'A' -> gradeA
        'B' -> gradeB
        'C' -> gradeC
        'D' -> gradeD
        'E' -> gradeE
        else -> unknown
    }
}
