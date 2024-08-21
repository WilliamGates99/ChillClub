package com.xeniac.chillclub.core.data.local.mapper

import com.xeniac.chillclub.core.data.local.dto.AppThemeDto
import com.xeniac.chillclub.core.domain.models.AppTheme

fun AppTheme.toAppThemeDto(): AppThemeDto = when (this) {
    AppTheme.Light -> AppThemeDto.Light
    AppTheme.Dark -> AppThemeDto.Dark
}

fun AppThemeDto.toAppTheme(): AppTheme = when (this) {
    AppThemeDto.Light -> AppTheme.Light
    AppThemeDto.Dark -> AppTheme.Dark
}