package com.xeniac.chillclub.feature_settings.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.xeniac.chillclub.R
import com.xeniac.chillclub.core.domain.models.AppTheme
import com.xeniac.chillclub.core.domain.utils.Result
import com.xeniac.chillclub.core.presentation.utils.Event
import com.xeniac.chillclub.core.presentation.utils.UiEvent
import com.xeniac.chillclub.core.presentation.utils.UiText
import com.xeniac.chillclub.feature_settings.domain.use_cases.SettingsUseCases
import com.xeniac.chillclub.feature_settings.domain.utils.AppThemeError
import com.xeniac.chillclub.feature_settings.domain.utils.PlayInBackgroundError
import com.xeniac.chillclub.feature_settings.presentation.states.SettingsState
import com.xeniac.chillclub.feature_settings.presentation.utils.SettingsUiEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.WhileSubscribed
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.time.Duration.Companion.seconds

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val settingsUseCases: SettingsUseCases
) : ViewModel() {

    private val _appTheme = settingsUseCases.getCurrentAppThemeUseCase.get()().stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(),
        initialValue = null
    )

    private val _isPlayInBackgroundEnabled = settingsUseCases.getIsPlayInBackgroundEnabledUseCase
        .get()().stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(),
        initialValue = null
    )

    private val _settingsState = MutableStateFlow(SettingsState())
    val settingsState = combine(
        flow = _settingsState,
        flow2 = _appTheme,
        flow3 = _isPlayInBackgroundEnabled
    ) { settingsState, appTheme, isPlayInBackgroundEnabled ->
        settingsState.copy(
            currentAppTheme = appTheme,
            isPlayInBackgroundEnabled = isPlayInBackgroundEnabled
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(stopTimeout = 5.seconds),
        initialValue = SettingsState()
    )

    private val _setAppThemeEventChannel = Channel<Event>()
    val setAppThemeEventChannel = _setAppThemeEventChannel.receiveAsFlow()

    private val _setPlayInBackgroundEventChannel = Channel<UiEvent>()
    val setPlayInBackgroundEventChannel = _setPlayInBackgroundEventChannel.receiveAsFlow()

    fun onAction(action: SettingsAction) {
        when (action) {
            is SettingsAction.StoreCurrentAppTheme -> storeCurrentAppTheme(action.newAppTheme)
            is SettingsAction.StorePlayInBackgroundSwitch -> storeNotificationSoundSwitch(action.isEnabled)
            is SettingsAction.OnPermissionResult -> onPermissionResult(
                permission = action.permission,
                isGranted = action.isGranted
            )
            is SettingsAction.DismissPermissionDialog -> dismissPermissionDialog(action.permission)
        }
    }

    private fun storeCurrentAppTheme(newAppTheme: AppTheme) = viewModelScope.launch {
        val shouldUpdateAppTheme = newAppTheme != settingsState.value.currentAppTheme
        if (shouldUpdateAppTheme) {
            when (val result = settingsUseCases.storeCurrentAppThemeUseCase.get()(newAppTheme)) {
                is Result.Success -> {
                    _setAppThemeEventChannel.send(SettingsUiEvent.UpdateAppTheme(newAppTheme))
                }
                is Result.Error -> {
                    when (result.error) {
                        is AppThemeError.SomethingWentWrong -> {
                            _setAppThemeEventChannel.send(
                                UiEvent.ShowShortSnackbar(UiText.StringResource(R.string.error_something_went_wrong))
                            )
                        }
                    }
                }
            }
        }
    }

    private fun storeNotificationSoundSwitch(isEnabled: Boolean) = viewModelScope.launch {
        when (val result =
            settingsUseCases.storeIsPlayInBackgroundEnabledUseCase.get()(isEnabled)) {
            is Result.Success -> Unit
            is Result.Error -> {
                when (result.error) {
                    PlayInBackgroundError.SomethingWentWrong -> {
                        _setPlayInBackgroundEventChannel.send(
                            UiEvent.ShowShortSnackbar(UiText.StringResource(R.string.error_something_went_wrong))
                        )
                    }
                }
            }
        }
    }

    private fun onPermissionResult(
        permission: String,
        isGranted: Boolean
    ) = viewModelScope.launch {
        val shouldAskForPermission = settingsState.value.run {
            !permissionDialogQueue.contains(permission) && !isGranted
        }

        if (shouldAskForPermission) {
            _settingsState.update {
                it.copy(
                    permissionDialogQueue = listOf(permission),
                    isPermissionDialogVisible = true
                )
            }
        }
    }

    private fun dismissPermissionDialog(permission: String) = viewModelScope.launch {
        _settingsState.update {
            it.copy(
                permissionDialogQueue = it.permissionDialogQueue.toMutableList().apply {
                    remove(permission)
                }.toList(),
                isPermissionDialogVisible = false
            )
        }
    }
}