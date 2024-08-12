package com.xeniac.chillclub.core.data.mapper

import com.xeniac.chillclub.core.data.dto.AppThemeDto
import com.xeniac.chillclub.core.domain.models.AppTheme

fun AppTheme.toAppThemeDto(): AppThemeDto = when (this) {
    AppTheme.Default -> AppThemeDto.Default
    AppTheme.Light -> AppThemeDto.Light
    AppTheme.Dark -> AppThemeDto.Dark
}

fun AppThemeDto.toAppTheme(): AppTheme = when (this) {
    AppThemeDto.Default -> AppTheme.Default
    AppThemeDto.Light -> AppTheme.Light
    AppThemeDto.Dark -> AppTheme.Dark
}