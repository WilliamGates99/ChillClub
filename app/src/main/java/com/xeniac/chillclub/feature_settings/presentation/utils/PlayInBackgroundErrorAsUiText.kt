package com.xeniac.chillclub.feature_settings.presentation.utils

import com.xeniac.chillclub.R
import com.xeniac.chillclub.core.presentation.utils.UiText
import com.xeniac.chillclub.feature_settings.domain.utils.PlayInBackgroundError

fun PlayInBackgroundError.asUiText(): UiText = when (this) {
    PlayInBackgroundError.SomethingWentWrong -> UiText.StringResource(R.string.error_something_went_wrong)
}