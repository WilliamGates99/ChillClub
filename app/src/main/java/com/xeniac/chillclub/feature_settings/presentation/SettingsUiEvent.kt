package com.xeniac.chillclub.feature_settings.presentation

import com.xeniac.chillclub.core.domain.models.AppTheme
import com.xeniac.chillclub.core.presentation.common.utils.Event

sealed class SettingsUiEvent : Event() {
    data class UpdateAppTheme(val newAppTheme: AppTheme) : SettingsUiEvent()
    data object RestartActivity : SettingsUiEvent()
}