package com.xeniac.chillclub.feature_settings.presentation

import com.xeniac.chillclub.core.domain.models.AppTheme

sealed interface SettingsEvent {
    data class SetCurrentAppTheme(val newAppTheme: AppTheme) : SettingsEvent
    data class SetPlayInBackgroundSwitch(val isEnabled: Boolean) : SettingsEvent
}