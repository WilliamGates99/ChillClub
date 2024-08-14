package com.xeniac.chillclub.feature_settings.domain.utils

import com.xeniac.chillclub.core.domain.utils.Error

sealed class AppThemeError : Error() {
    data object SomethingWentWrong : AppThemeError()
}