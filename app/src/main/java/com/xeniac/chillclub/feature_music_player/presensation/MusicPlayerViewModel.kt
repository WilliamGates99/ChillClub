package com.xeniac.chillclub.feature_music_player.presensation

import android.graphics.Rect
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.xeniac.chillclub.core.domain.models.RadioStation
import com.xeniac.chillclub.core.domain.repositories.ConnectivityObserver
import com.xeniac.chillclub.core.domain.utils.Result
import com.xeniac.chillclub.core.presentation.utils.Event
import com.xeniac.chillclub.core.presentation.utils.NetworkObserverHelper
import com.xeniac.chillclub.core.presentation.utils.UiEvent
import com.xeniac.chillclub.feature_music_player.domain.repositories.MusicVolumePercentage
import com.xeniac.chillclub.feature_music_player.domain.use_cases.MusicPlayerUseCases
import com.xeniac.chillclub.feature_music_player.presensation.states.MusicPlayerState
import com.xeniac.chillclub.feature_music_player.presensation.utils.asUiText
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.WhileSubscribed
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.time.Duration.Companion.seconds

@HiltViewModel
class MusicPlayerViewModel @Inject constructor(
    private val musicPlayerUseCases: MusicPlayerUseCases
) : ViewModel() {

    private val _musicPlayerState = MutableStateFlow(MusicPlayerState())
    val musicPlayerState = combine(
        flow = _musicPlayerState,
        flow2 = musicPlayerUseCases.observeMusicVolumeChangesUseCase.get()(),
        flow3 = musicPlayerUseCases.getCurrentlyPlayingRadioStationIdUseCase.get()(),
        flow4 = musicPlayerUseCases.getIsPlayInBackgroundEnabledUseCase.get()(),
        flow5 = musicPlayerUseCases.getNotificationPermissionCountUseCase.get()()
    ) { musicPlayerState, musicVolumePercentage, currentlyPlayingRadioStationId, isPlayInBackgroundEnabled, notificationPermissionCount ->
        musicPlayerState.copy(
            musicVolumePercentage = musicVolumePercentage,
            currentRadioStationId = currentlyPlayingRadioStationId,
            isPlayInBackgroundEnabled = isPlayInBackgroundEnabled,
            notificationPermissionCount = notificationPermissionCount
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(stopTimeout = 5.seconds),
        initialValue = MusicPlayerState()
    )

    private val _getRadioStationsEventChannel = Channel<Event>()
    val getRadioStationsEventChannel = _getRadioStationsEventChannel.receiveAsFlow()

    private val _playMusicEventChannel = Channel<UiEvent>()
    val playMusicEventChannel = _playMusicEventChannel.receiveAsFlow()

    private val _adjustMusicVolumeEventChannel = Channel<UiEvent>()
    val adjustMusicVolumeEventChannel = _adjustMusicVolumeEventChannel.receiveAsFlow()

    init {
        getRadioStations()
    }

    fun onAction(action: MusicPlayerAction) {
        when (action) {
            MusicPlayerAction.GetRadioStations -> getRadioStations()
            MusicPlayerAction.GetCurrentRadioStation -> getCurrentRadioStation()
            is MusicPlayerAction.PlayMusic -> playMusic(action.radiosStation)
            MusicPlayerAction.PauseMusic -> pauseMusic()
            MusicPlayerAction.ShowVolumeSlider -> showVolumeSlider()
            MusicPlayerAction.HideVolumeSlider -> hideVolumeSlider()
            is MusicPlayerAction.SetVolumeSliderBounds -> setVolumeSliderBounds(action.bounds)
            is MusicPlayerAction.AdjustMusicVolume -> adjustMusicVolume(action.newPercentage)
            is MusicPlayerAction.OnPermissionResult -> onPermissionResult(
                permission = action.permission,
                isGranted = action.isGranted
            )
            is MusicPlayerAction.DismissPermissionDialog -> dismissPermissionDialog(action.permission)
        }
    }

    private fun getRadioStations() {
        musicPlayerUseCases.getRadioStationsUseCase.get()(
            fetchFromRemote = NetworkObserverHelper.networkStatus.value == ConnectivityObserver.Status.AVAILABLE
        ).onStart {
            _musicPlayerState.update {
                it.copy(isRadioStationsLoading = true)
            }
        }.onEach { getRadioStationsResult ->
            when (getRadioStationsResult) {
                is Result.Success -> {
                    getRadioStationsResult.data.let { radioStations ->
                        _musicPlayerState.update {
                            it.copy(
                                radioStations = radioStations,
                                isRadioStationsLoading = false
                            )
                        }
                    }
                }
                is Result.Error -> {
                    when (val error = getRadioStationsResult.error) {
//                        GetRadioStationsError.Network.Offline -> TODO()
//                        GetRadioStationsError.Network.ConnectTimeoutException -> TODO()
//                        GetRadioStationsError.Network.HttpRequestTimeoutException -> TODO()
//                        GetRadioStationsError.Network.SocketTimeoutException -> TODO()
//                        GetRadioStationsError.Network.RedirectResponseException -> TODO()
//                        GetRadioStationsError.Network.ClientRequestException -> TODO()
//                        GetRadioStationsError.Network.ServerResponseException -> TODO()
//                        GetRadioStationsError.Network.SerializationException -> TODO()
//                        GetRadioStationsError.Network.SomethingWentWrong -> TODO()
//                        GetRadioStationsError.Local.SomethingWentWrong -> TODO()
                        else -> {
                            _getRadioStationsEventChannel.send(
                                UiEvent.ShowLongSnackbar(error.asUiText())
                            )
                        }
                    }
                }
            }
        }.onCompletion { throwable ->
            if (throwable == null) {
                _musicPlayerState.update {
                    it.copy(isRadioStationsLoading = false)
                }
            }

            if (throwable is CancellationException) {
                _musicPlayerState.update {
                    it.copy(isRadioStationsLoading = false)
                }
            }
        }.launchIn(scope = viewModelScope)
    }

    private fun getCurrentRadioStation() {
        musicPlayerUseCases.getCurrentlyPlayingRadioStationUseCase.get()(
            radioStationId = musicPlayerState.value.currentRadioStationId
        ).onEach { radioStation ->
            _musicPlayerState.update {
                it.copy(
                    currentRadioStation = radioStation
                )
            }
        }.launchIn(scope = viewModelScope)
    }

    private fun playMusic(radiosStation: RadioStation?) = viewModelScope.launch {
        // TODO: MODIFY AFTER ADDING YOUTUBE PLAYER
        radiosStation?.id?.let { id ->
            when (val result = musicPlayerUseCases.storeCurrentlyPlayingRadioStationIdUseCase.get()(
                radioStationId = id
            )) {
                is Result.Success -> {
                    _musicPlayerState.update {
                        it.copy(isMusicPlaying = true)
                    }
                }
                is Result.Error -> {
                    _playMusicEventChannel.send(
                        UiEvent.ShowShortSnackbar(result.error.asUiText())
                    )
                }
            }
        }
    }

    private fun pauseMusic() {
        // TODO: MODIFY AFTER ADDING YOUTUBE PLAYER
        _musicPlayerState.update {
            it.copy(isMusicPlaying = false)
        }
    }

    private fun showVolumeSlider() {
        _musicPlayerState.update {
            it.copy(isVolumeSliderVisible = true)
        }
    }

    private fun hideVolumeSlider() {
        _musicPlayerState.update {
            it.copy(isVolumeSliderVisible = false)
        }
    }

    private fun setVolumeSliderBounds(bounds: Rect) {
        _musicPlayerState.update {
            it.copy(volumeSliderBounds = bounds)
        }
    }

    private fun adjustMusicVolume(newPercentage: MusicVolumePercentage) {
        musicPlayerUseCases.adjustMusicVolumeUseCase.get()(
            newPercentage = newPercentage,
            currentPercentage = musicPlayerState.value.musicVolumePercentage
        ).onEach { result ->
            when (result) {
                is Result.Success -> Unit
                is Result.Error -> {
                    _adjustMusicVolumeEventChannel.send(
                        UiEvent.ShowLongSnackbar(result.error.asUiText())
                    )
                }
            }
        }.launchIn(viewModelScope)
    }

    private fun onPermissionResult(
        permission: String,
        isGranted: Boolean
    ) {
        val shouldAskForPermission = musicPlayerState.value.run {
            notificationPermissionCount < 1 && !permissionDialogQueue.contains(permission) && !isGranted
        }

        if (shouldAskForPermission) {
            _musicPlayerState.update {
                it.copy(
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

        _musicPlayerState.update {
            it.copy(
                permissionDialogQueue = musicPlayerState.value.permissionDialogQueue.toMutableList()
                    .apply { remove(permission) }.toList(),
                isPermissionDialogVisible = false
            )
        }
    }
}