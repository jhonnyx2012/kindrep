package com.jhonnyb.kindrep.ui.theme

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

@Immutable
data class KindrepPalette(
    val isDark: Boolean,
    val canvas: Color,
    val surface: Color,
    val surfaceVariant: Color,
    val border: Color,
    val primary: Color,
    val onPrimary: Color,
    val secondary: Color,
    val secondarySoft: Color,
    val textPrimary: Color,
    val textMuted: Color,
    val figureBody: Color,
    val mascotBody: Color,
    val mascotStroke: Color,
    val mascotEyes: Color,
    val mascotAccent: Color,
    val matFill: Color,
    val iconBackground: Color,
    val iconLine: Color,
)

val WarmEarthPalette = KindrepPalette(
    isDark = false,
    canvas = Color(0xFFF7F5F0),
    surface = Color(0xFFEFECE4),
    surfaceVariant = Color(0xFFE6E1D6),
    border = Color(0xFFDBD5C9),
    primary = Color(0xFF5B7B6A),
    onPrimary = Color(0xFFFFFFFF),
    secondary = Color(0xFFC87D55),
    secondarySoft = Color(0xFFEFE0D5),
    textPrimary = Color(0xFF232524),
    textMuted = Color(0xFF585D5A),
    figureBody = Color(0xFF94989B),
    mascotBody = Color(0xFFFBF9F4),
    mascotStroke = Color(0xFF3E564B),
    mascotEyes = Color(0xFF3E564B),
    mascotAccent = Color(0xFFC87D55),
    matFill = Color(0x475B7B6A),
    iconBackground = Color(0xFF5B7B6A),
    iconLine = Color(0xFFF7F5F0),
)

val SoftStoneD3Palette = KindrepPalette(
    isDark = true,
    canvas = Color(0xFF212624),
    surface = Color(0xFF313835),
    surfaceVariant = Color(0xFF3E4541),
    border = Color(0xFF4D5752),
    primary = Color(0xFF96C2B0),
    onPrimary = Color(0xFF18221D),
    secondary = Color(0xFFE0A26E),
    secondarySoft = Color(0xFF463A30),
    textPrimary = Color(0xFFF5F4F0),
    textMuted = Color(0xFFB6BCB8),
    figureBody = Color(0xFFD0D4D6),
    mascotBody = Color(0xFFF2EEE6),
    mascotStroke = Color(0xFF648977),
    mascotEyes = Color(0xFF3E564B),
    mascotAccent = Color(0xFFE0975F),
    matFill = Color(0x5796C2B0),
    iconBackground = Color(0xFF3B5246),
    iconLine = Color(0xFFEAF2EE),
)

val LocalKindrepPalette = staticCompositionLocalOf { WarmEarthPalette }
