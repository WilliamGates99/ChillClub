package com.xeniac.chillclub.feature_settings.presentation.utils

import com.xeniac.chillclub.R
import com.xeniac.chillclub.core.presentation.common.utils.UiText
import com.xeniac.chillclub.feature_settings.domain.errors.StoreIsPlayInBackgroundEnabledError

fun StoreIsPlayInBackgroundEnabledError.asUiText(): UiText = when (this) {
    StoreIsPlayInBackgroundEnabledError.SomethingWentWrong -> UiText.StringResource(R.string.error_something_went_wrong)
}