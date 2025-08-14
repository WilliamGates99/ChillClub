package com.xeniac.chillclub.feature_settings.domain.errors

import com.xeniac.chillclub.core.domain.errors.Error

sealed class StoreCurrentAppThemeError : Error() {
    data object SomethingWentWrong : StoreCurrentAppThemeError()
}