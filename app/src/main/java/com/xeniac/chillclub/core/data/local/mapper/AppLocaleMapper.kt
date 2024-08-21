package com.xeniac.chillclub.core.data.local.mapper

import com.xeniac.chillclub.core.data.local.dto.AppLocaleDto
import com.xeniac.chillclub.core.domain.models.AppLocale

fun AppLocale.toAppLocaleDto(): AppLocaleDto = when (this) {
    AppLocale.Default -> AppLocaleDto.Default
    AppLocale.EnglishGB -> AppLocaleDto.EnglishGB
    AppLocale.EnglishUS -> AppLocaleDto.EnglishUS
    AppLocale.FarsiIR -> AppLocaleDto.FarsiIR
}

fun AppLocaleDto.toAppLocale(): AppLocale = when (this) {
    AppLocaleDto.Default -> AppLocale.Default
    AppLocaleDto.EnglishGB -> AppLocale.EnglishGB
    AppLocaleDto.EnglishUS -> AppLocale.EnglishUS
    AppLocaleDto.FarsiIR -> AppLocale.FarsiIR
}