package com.xeniac.chillclub.feature_settings.presentation

import com.xeniac.chillclub.core.domain.models.AppTheme

sealed interface SettingsEvent {
    data class StoreCurrentAppTheme(val newAppTheme: AppTheme) : SettingsEvent
    data class StorePlayInBackgroundSwitch(val isEnabled: Boolean) : SettingsEvent

    data class OnPermissionResult(
        val permission: String,
        val isGranted: Boolean
    ) : SettingsEvent

    data class DismissPermissionDialog(val permission: String) : SettingsEvent
}