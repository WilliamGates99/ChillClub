package com.xeniac.chillclub.core.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import com.xeniac.chillclub.R

val chillClubFont = FontFamily(
    listOf(
        Font(resId = R.font.font_thin, weight = FontWeight.Thin),
        Font(resId = R.font.font_extra_light, weight = FontWeight.ExtraLight),
        Font(resId = R.font.font_light, weight = FontWeight.Light),
        Font(resId = R.font.font_regular, weight = FontWeight.Normal),
        Font(resId = R.font.font_medium, weight = FontWeight.Medium),
        Font(resId = R.font.font_semi_bold, weight = FontWeight.SemiBold),
        Font(resId = R.font.font_bold, weight = FontWeight.Bold),
        Font(resId = R.font.font_extra_bold, weight = FontWeight.ExtraBold),
        Font(resId = R.font.font_black, weight = FontWeight.Black)
    )
)

private val defaultTypography = Typography()
val Typography = Typography(
    displayLarge = defaultTypography.displayLarge.copy(fontFamily = chillClubFont),
    displayMedium = defaultTypography.displayMedium.copy(fontFamily = chillClubFont),
    displaySmall = defaultTypography.displaySmall.copy(fontFamily = chillClubFont),
    headlineLarge = defaultTypography.headlineLarge.copy(fontFamily = chillClubFont),
    headlineMedium = defaultTypography.headlineMedium.copy(fontFamily = chillClubFont),
    headlineSmall = defaultTypography.headlineSmall.copy(fontFamily = chillClubFont),
    titleLarge = defaultTypography.titleLarge.copy(fontFamily = chillClubFont),
    titleMedium = defaultTypography.titleMedium.copy(fontFamily = chillClubFont),
    titleSmall = defaultTypography.titleSmall.copy(fontFamily = chillClubFont),
    bodyLarge = defaultTypography.bodyLarge.copy(fontFamily = chillClubFont),
    bodyMedium = defaultTypography.bodyMedium.copy(fontFamily = chillClubFont),
    bodySmall = defaultTypography.bodySmall.copy(fontFamily = chillClubFont),
    labelLarge = defaultTypography.labelLarge.copy(fontFamily = chillClubFont),
    labelMedium = defaultTypography.labelMedium.copy(fontFamily = chillClubFont),
    labelSmall = defaultTypography.labelSmall.copy(fontFamily = chillClubFont)
)