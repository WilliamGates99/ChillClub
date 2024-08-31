package com.xeniac.chillclub.feature_music_player.presensation

import android.media.AudioManager
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.xeniac.chillclub.core.domain.repositories.ConnectivityObserver
import com.xeniac.chillclub.core.domain.utils.Result
import com.xeniac.chillclub.core.presentation.utils.Event
import com.xeniac.chillclub.core.presentation.utils.NetworkObserverHelper
import com.xeniac.chillclub.core.presentation.utils.UiEvent
import com.xeniac.chillclub.feature_music_player.domain.use_cases.MusicPlayerUseCases
import com.xeniac.chillclub.feature_music_player.presensation.states.MusicPlayerState
import com.xeniac.chillclub.feature_music_player.presensation.utils.asUiText
import dagger.Lazy
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.WhileSubscribed
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import javax.inject.Inject
import kotlin.time.Duration.Companion.seconds

@HiltViewModel
class MusicPlayerViewModel @Inject constructor(
    private val musicPlayerUseCases: MusicPlayerUseCases,
    private val savedStateHandle: SavedStateHandle,
    private val audioManager: Lazy<AudioManager>
) : ViewModel() {

    private val mutex: Mutex = Mutex()

    private val _musicVolume =
        musicPlayerUseCases.observeMusicVolumeChangesUseCase.get()().stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(),
            initialValue = null
        )

    private val _notificationPermissionCount =
        musicPlayerUseCases.getNotificationPermissionCountUseCase.get()().stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(),
            initialValue = 0
        )

    private val _musicPlayerState = savedStateHandle.getStateFlow(
        key = "musicPlayerState",
        initialValue = MusicPlayerState()
    )
    val musicPlayerState = combine(
        flow = _musicPlayerState,
        flow2 = _musicVolume,
        flow3 = _notificationPermissionCount
    ) { musicPlayerState, musicVolume, notificationPermissionCount ->
        musicPlayerState.copy(
            musicVolume = musicVolume,
            notificationPermissionCount = notificationPermissionCount
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(stopTimeout = 5.seconds),
        initialValue = MusicPlayerState()
    )

    private val _getRadiosEventChannel = Channel<Event>()
    val getRadiosEventChannel = _getRadiosEventChannel.receiveAsFlow()

    private val _adjustMusicVolumeEventChannel = Channel<UiEvent>()
    val adjustMusicVolumeEventChannel = _adjustMusicVolumeEventChannel.receiveAsFlow()

    init {
        getRadios()
    }

    fun onAction(action: MusicPlayerAction) {
        when (action) {
            MusicPlayerAction.GetRadios -> getRadios()
            MusicPlayerAction.DecreaseMusicVolume -> decreaseMusicVolume()
            MusicPlayerAction.IncreaseMusicVolume -> increaseMusicVolume()
            is MusicPlayerAction.OnPermissionResult -> onPermissionResult(
                permission = action.permission,
                isGranted = action.isGranted
            )
            is MusicPlayerAction.DismissPermissionDialog -> dismissPermissionDialog(action.permission)
        }
    }

    private fun getRadios() = viewModelScope.launch {
        mutex.withLock {
            savedStateHandle["musicPlayerState"] = musicPlayerState.value.copy(
                isRadiosLoading = true
            )
        }

        musicPlayerUseCases.getRadiosUseCase.get()(
            fetchFromRemote = NetworkObserverHelper.networkStatus.value == ConnectivityObserver.Status.AVAILABLE
        ).collect { getRadiosResult ->
            when (getRadiosResult) {
                is Result.Success -> {
                    getRadiosResult.data.let { radios ->
                        mutex.withLock {
                            savedStateHandle["musicPlayerState"] = musicPlayerState.value.copy(
                                radios = radios,
                                isRadiosLoading = false
                            )
                        }
                    }
                }
                is Result.Error -> {
                    when (val error = getRadiosResult.error) {
//                        GetRadiosError.Network.Offline -> TODO()
//                        GetRadiosError.Network.ConnectTimeoutException -> TODO()
//                        GetRadiosError.Network.HttpRequestTimeoutException -> TODO()
//                        GetRadiosError.Network.SocketTimeoutException -> TODO()
//                        GetRadiosError.Network.RedirectResponseException -> TODO()
//                        GetRadiosError.Network.ClientRequestException -> TODO()
//                        GetRadiosError.Network.ServerResponseException -> TODO()
//                        GetRadiosError.Network.SerializationException -> TODO()
//                        GetRadiosError.Network.SomethingWentWrong -> TODO()
//                        GetRadiosError.Local.SomethingWentWrong -> TODO()
                        else -> {
                            _getRadiosEventChannel.send(
                                UiEvent.ShowLongSnackbar(error.asUiText())
                            )
                        }
                    }

                    mutex.withLock {
                        savedStateHandle["musicPlayerState"] = musicPlayerState.value.copy(
                            isRadiosLoading = false
                        )
                    }
                }
            }
        }
    }

    private fun decreaseMusicVolume() = viewModelScope.launch {
        musicPlayerUseCases.decreaseMusicVolumeUseCase.get()().collect { result ->
            when (result) {
                is Result.Success -> Unit
                is Result.Error -> {
                    _adjustMusicVolumeEventChannel.send(
                        UiEvent.ShowLongSnackbar(result.error.asUiText())
                    )
                }
            }
        }
    }

    private fun increaseMusicVolume() = viewModelScope.launch {
        musicPlayerUseCases.increaseMusicVolumeUseCase.get()().collect { result ->
            when (result) {
                is Result.Success -> Unit
                is Result.Error -> {
                    _adjustMusicVolumeEventChannel.send(
                        UiEvent.ShowLongSnackbar(result.error.asUiText())
                    )
                }
            }
        }
    }

    private fun onPermissionResult(
        permission: String,
        isGranted: Boolean
    ) = viewModelScope.launch {
        val shouldAskForPermission = musicPlayerState.value.run {
            notificationPermissionCount < 1 && !permissionDialogQueue.contains(permission) && !isGranted
        }

        if (shouldAskForPermission) {
            mutex.withLock {
                savedStateHandle["musicPlayerState"] = musicPlayerState.value.copy(
                    permissionDialogQueue = listOf(permission),
                    isPermissionDialogVisible = true
                )
            }
        }
    }

    private fun dismissPermissionDialog(permission: String) = viewModelScope.launch {
        musicPlayerUseCases.storeNotificationPermissionCountUseCase.get()(
            count = musicPlayerState.value.notificationPermissionCount.plus(1)
        )

        mutex.withLock {
            savedStateHandle["musicPlayerState"] = musicPlayerState.value.copy(
                permissionDialogQueue = musicPlayerState.value.permissionDialogQueue.toMutableList()
                    .apply { remove(permission) }.toList(),
                isPermissionDialogVisible = false
            )
        }
    }
}