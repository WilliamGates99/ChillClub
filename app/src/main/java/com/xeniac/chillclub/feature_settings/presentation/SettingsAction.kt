package com.xeniac.chillclub.feature_settings.presentation

import com.xeniac.chillclub.core.domain.models.AppTheme

sealed interface SettingsAction {
    data class StoreCurrentAppTheme(val newAppTheme: AppTheme) : SettingsAction
    data class StorePlayInBackgroundSwitch(val isEnabled: Boolean) : SettingsAction

    data class OnPermissionResult(
        val permission: String,
        val isGranted: Boolean
    ) : SettingsAction

    data class DismissPermissionDialog(val permission: String) : SettingsAction
}