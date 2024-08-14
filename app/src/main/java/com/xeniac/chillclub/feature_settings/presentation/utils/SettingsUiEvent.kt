package com.xeniac.chillclub.feature_settings.presentation.utils

import com.xeniac.chillclub.core.domain.models.AppTheme
import com.xeniac.chillclub.core.presentation.utils.Event

sealed class SettingsUiEvent : Event() {
    data class UpdateAppTheme(val newAppTheme: AppTheme) : SettingsUiEvent()
    data object RestartActivity : SettingsUiEvent()
}