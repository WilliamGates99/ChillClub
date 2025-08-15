package com.xeniac.chillclub.feature_settings.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.xeniac.chillclub.core.domain.models.AppTheme
import com.xeniac.chillclub.core.domain.models.Result
import com.xeniac.chillclub.core.presentation.common.utils.Event
import com.xeniac.chillclub.core.presentation.common.utils.UiEvent
import com.xeniac.chillclub.feature_settings.domain.use_cases.SettingsUseCases
import com.xeniac.chillclub.feature_settings.presentation.states.SettingsState
import com.xeniac.chillclub.feature_settings.presentation.utils.asUiText
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.WhileSubscribed
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
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

    private val _state = MutableStateFlow(SettingsState())
    val state = combine(
        flow = _state,
        flow2 = settingsUseCases.getCurrentAppThemeUseCase.get()(),
        flow3 = settingsUseCases.getIsPlayInBackgroundEnabledUseCase.get()()
    ) { state, appTheme, isPlayInBackgroundEnabled ->
        _state.update {
            state.copy(
                currentAppTheme = appTheme,
                isPlayInBackgroundEnabled = isPlayInBackgroundEnabled
            )
        }

        _state.value
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(stopTimeout = 30.seconds),
        initialValue = _state.value
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

    private fun storeCurrentAppTheme(
        newAppTheme: AppTheme
    ) {
        val shouldUpdateAppTheme = newAppTheme != _state.value.currentAppTheme
        if (shouldUpdateAppTheme) {
            settingsUseCases.storeCurrentAppThemeUseCase.get()(
                newAppTheme = newAppTheme
            ).onEach { result ->
                when (result) {
                    is Result.Success -> {
                        _setAppThemeEventChannel.send(
                            SettingsUiEvent.UpdateAppTheme(newAppTheme = newAppTheme)
                        )
                    }
                    is Result.Error -> {
                        _setAppThemeEventChannel.send(
                            UiEvent.ShowShortSnackbar(result.error.asUiText())
                        )
                    }
                }
            }.launchIn(scope = viewModelScope)
        }
    }

    private fun storeNotificationSoundSwitch(
        isEnabled: Boolean
    ) {
        settingsUseCases.storeIsPlayInBackgroundEnabledUseCase.get()(
            isEnabled = isEnabled
        ).onEach { result ->
            when (result) {
                is Result.Success -> Unit
                is Result.Error -> {
                    _setPlayInBackgroundEventChannel.send(
                        UiEvent.ShowShortSnackbar(result.error.asUiText())
                    )
                }
            }
        }.launchIn(scope = viewModelScope)
    }

    private fun onPermissionResult(
        permission: String,
        isGranted: Boolean
    ) = viewModelScope.launch {
        val shouldAskForPermission = _state.value.run {
            !permissionDialogQueue.contains(permission) && !isGranted
        }

        if (shouldAskForPermission) {
            _state.update {
                it.copy(
                    permissionDialogQueue = listOf(permission),
                    isPermissionDialogVisible = true
                )
            }
        }
    }

    private fun dismissPermissionDialog(
        permission: String
    ) = viewModelScope.launch {
        _state.update {
            it.copy(
                permissionDialogQueue = it.permissionDialogQueue.toMutableList().apply {
                    remove(permission)
                },
                isPermissionDialogVisible = false
            )
        }
    }
}