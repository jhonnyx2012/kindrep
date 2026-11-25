package com.jhonnyb.kindrep.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

val KindrepShapes = Shapes(
    extraSmall = RoundedCornerShape(9.dp),
    small = RoundedCornerShape(12.dp),
    medium = RoundedCornerShape(14.dp),
    large = RoundedCornerShape(18.dp),
    extraLarge = RoundedCornerShape(18.dp),
)

private val WarmEarthMaterialScheme = lightColorScheme(
    primary = WarmEarthPalette.primary,
    onPrimary = WarmEarthPalette.onPrimary,
    secondary = WarmEarthPalette.secondary,
    onSecondary = Color.White,
    secondaryContainer = WarmEarthPalette.secondarySoft,
    onSecondaryContainer = WarmEarthPalette.secondary,
    background = WarmEarthPalette.canvas,
    onBackground = WarmEarthPalette.textPrimary,
    surface = WarmEarthPalette.surface,
    onSurface = WarmEarthPalette.textPrimary,
    surfaceVariant = WarmEarthPalette.surfaceVariant,
    onSurfaceVariant = WarmEarthPalette.textMuted,
    outline = WarmEarthPalette.border,
)

private val SoftStoneD3MaterialScheme = darkColorScheme(
    primary = SoftStoneD3Palette.primary,
    onPrimary = SoftStoneD3Palette.onPrimary,
    secondary = SoftStoneD3Palette.secondary,
    onSecondary = SoftStoneD3Palette.onPrimary,
    secondaryContainer = SoftStoneD3Palette.secondarySoft,
    onSecondaryContainer = SoftStoneD3Palette.secondary,
    background = SoftStoneD3Palette.canvas,
    onBackground = SoftStoneD3Palette.textPrimary,
    surface = SoftStoneD3Palette.surface,
    onSurface = SoftStoneD3Palette.textPrimary,
    surfaceVariant = SoftStoneD3Palette.surfaceVariant,
    onSurfaceVariant = SoftStoneD3Palette.textMuted,
    outline = SoftStoneD3Palette.border,
)

@Composable
fun KindrepTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit,
) {
    val palette: KindrepPalette = if (darkTheme) SoftStoneD3Palette else WarmEarthPalette
    val colorScheme: ColorScheme = if (darkTheme) SoftStoneD3MaterialScheme else WarmEarthMaterialScheme

    CompositionLocalProvider(LocalKindrepPalette provides palette) {
        MaterialTheme(
            colorScheme = colorScheme,
            typography = kindrepTypography(),
            shapes = KindrepShapes,
            content = content,
        )
    }
}
