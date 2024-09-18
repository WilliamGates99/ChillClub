package com.xeniac.chillclub.feature_settings.presentation.states

import android.os.Parcelable
import com.xeniac.chillclub.core.domain.models.AppTheme
import kotlinx.parcelize.Parcelize

@Parcelize
data class SettingsState(
    val currentAppTheme: AppTheme? = null,
    val isPlayInBackgroundEnabled: Boolean? = null,
    val permissionDialogQueue: List<String> = emptyList(),
    val isPermissionDialogVisible: Boolean = false
) : Parcelable