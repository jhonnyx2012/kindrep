package com.jhonnybarrios.kindrep.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.Typography
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jhonnybarrios.kindrep.generated.resources.Res
import com.jhonnybarrios.kindrep.generated.resources.nunito_bold
import com.jhonnybarrios.kindrep.generated.resources.nunito_medium
import com.jhonnybarrios.kindrep.generated.resources.nunito_regular
import com.jhonnybarrios.kindrep.generated.resources.nunito_semibold
import org.jetbrains.compose.resources.Font

@Immutable
data class KindrepPalette(
    val isDark: Boolean,
    val canvas: Color,
    val surface: Color,
    val surfaceAlt: Color,
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
    val iconBg: Color,
    val iconLine: Color,
)

val WarmEarthPalette = KindrepPalette(
    isDark = false,
    canvas = Color(0xFFF7F5F0),
    surface = Color(0xFFEFECE4),
    surfaceAlt = Color(0xFFE6E1D6),
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
    iconBg = Color(0xFF5B7B6A),
    iconLine = Color(0xFFF7F5F0),
)

// Locked Soft Stone D3 Dark Mode + Option C1 Mascot Treatment
val SoftStoneD3Palette = KindrepPalette(
    isDark = true,
    canvas = Color(0xFF212624),
    surface = Color(0xFF313835),
    surfaceAlt = Color(0xFF3E4541),
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
    iconBg = Color(0xFF3B5246),
    iconLine = Color(0xFFEAF2EE),
)

val LocalKindrepPalette = staticCompositionLocalOf { WarmEarthPalette }

// Locked Tuned Radii: 9dp badges, 12dp rows/inputs, 14dp cards/CTAs/nav pill, 18dp sheets/modals
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
    surfaceVariant = WarmEarthPalette.surfaceAlt,
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
    surfaceVariant = SoftStoneD3Palette.surfaceAlt,
    onSurfaceVariant = SoftStoneD3Palette.textMuted,
    outline = SoftStoneD3Palette.border,
)

@Composable
fun kindrepFontFamily(): FontFamily = FontFamily(
    Font(Res.font.nunito_regular, FontWeight.Normal),
    Font(Res.font.nunito_medium, FontWeight.Medium),
    Font(Res.font.nunito_semibold, FontWeight.SemiBold),
    Font(Res.font.nunito_bold, FontWeight.Bold),
)

@Composable
fun kindrepTypography(): Typography {
    val nunito = kindrepFontFamily()
    return Typography(
        displayLarge = TextStyle(
            fontFamily = nunito,
            fontWeight = FontWeight.Bold,
            fontSize = 44.sp,
            lineHeight = 50.sp,
            fontFeatureSettings = "tnum",
        ),
        headlineLarge = TextStyle(
            fontFamily = nunito,
            fontWeight = FontWeight.Bold,
            fontSize = 28.sp,
            lineHeight = 34.sp,
        ),
        headlineMedium = TextStyle(
            fontFamily = nunito,
            fontWeight = FontWeight.Bold,
            fontSize = 24.sp,
            lineHeight = 30.sp,
        ),
        titleLarge = TextStyle(
            fontFamily = nunito,
            fontWeight = FontWeight.Bold,
            fontSize = 20.sp,
            lineHeight = 26.sp,
        ),
        titleMedium = TextStyle(
            fontFamily = nunito,
            fontWeight = FontWeight.SemiBold,
            fontSize = 17.sp,
            lineHeight = 23.sp,
            fontFeatureSettings = "tnum",
        ),
        bodyLarge = TextStyle(
            fontFamily = nunito,
            fontWeight = FontWeight.Normal,
            fontSize = 16.sp,
            lineHeight = 23.sp,
            fontFeatureSettings = "tnum",
        ),
        bodyMedium = TextStyle(
            fontFamily = nunito,
            fontWeight = FontWeight.Normal,
            fontSize = 14.sp,
            lineHeight = 20.sp,
            fontFeatureSettings = "tnum",
        ),
        labelLarge = TextStyle(
            fontFamily = nunito,
            fontWeight = FontWeight.Bold,
            fontSize = 16.sp,
            lineHeight = 20.sp,
            fontFeatureSettings = "tnum",
        ),
        labelMedium = TextStyle(
            fontFamily = nunito,
            fontWeight = FontWeight.SemiBold,
            fontSize = 14.sp,
            lineHeight = 18.sp,
            fontFeatureSettings = "tnum",
        ),
    )
}

@Composable
fun KindrepTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit,
) {
    val palette = if (darkTheme) SoftStoneD3Palette else WarmEarthPalette
    val colorScheme = if (darkTheme) SoftStoneD3MaterialScheme else WarmEarthMaterialScheme

    CompositionLocalProvider(LocalKindrepPalette provides palette) {
        MaterialTheme(
            colorScheme = colorScheme,
            typography = kindrepTypography(),
            shapes = KindrepShapes,
            content = content,
        )
    }
}
