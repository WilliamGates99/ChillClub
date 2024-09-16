package com.xeniac.chillclub.feature_settings.presentation.utils

import com.xeniac.chillclub.R
import com.xeniac.chillclub.core.presentation.utils.UiText
import com.xeniac.chillclub.feature_settings.domain.utils.AppThemeError

fun AppThemeError.asUiText(): UiText = when (this) {
    AppThemeError.SomethingWentWrong -> UiText.StringResource(R.string.error_something_went_wrong)
}