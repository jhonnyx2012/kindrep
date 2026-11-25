package com.jhonnyb.kindrep.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.jhonnyb.kindrep.generated.resources.Res
import com.jhonnyb.kindrep.generated.resources.nunito_bold
import com.jhonnyb.kindrep.generated.resources.nunito_medium
import com.jhonnyb.kindrep.generated.resources.nunito_regular
import com.jhonnyb.kindrep.generated.resources.nunito_semibold
import org.jetbrains.compose.resources.Font

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
