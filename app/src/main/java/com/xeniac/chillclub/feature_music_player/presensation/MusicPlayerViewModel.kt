package com.xeniac.chillclub.feature_music_player.presensation

import android.graphics.Rect
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.xeniac.chillclub.core.domain.models.RadioStation
import com.xeniac.chillclub.core.domain.models.SocialLinks
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
    private val musicPlayerUseCases: MusicPlayerUseCases,
//    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

//    private val mutex: Mutex = Mutex()

    private val _musicPlayerState = MutableStateFlow(MusicPlayerState())
    val musicPlayerState = combine(
        flow = _musicPlayerState,
        flow2 = musicPlayerUseCases.observeMusicVolumeChangesUseCase.get()(),
        flow3 = musicPlayerUseCases.getNotificationPermissionCountUseCase.get()()
    ) { musicPlayerState, musicVolumePercentage, notificationPermissionCount ->
        musicPlayerState.copy(
            musicVolumePercentage = musicVolumePercentage,
            notificationPermissionCount = notificationPermissionCount
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(stopTimeout = 5.seconds),
        initialValue = MusicPlayerState()
    )

    private val _getRadioStationsEventChannel = Channel<Event>()
    val getRadioStationsEventChannel = _getRadioStationsEventChannel.receiveAsFlow()

    private val _adjustMusicVolumeEventChannel = Channel<UiEvent>()
    val adjustMusicVolumeEventChannel = _adjustMusicVolumeEventChannel.receiveAsFlow()

    init {
        getRadioStations()

        // TODO: TEMP - REMOVE
        viewModelScope.launch {
            val station = RadioStation(
                youtubeVideoId = "videoId_1",
                title = "Test Title",
                channel = com.xeniac.chillclub.core.domain.models.Channel(
                    name = "Test Channel",
                    avatarUrl = "https://gravatar.com/avatar/b555351fc297b35d3eb1a7857740accd?s=800&d=mp&r=x",
                    socialLinks = SocialLinks(
                        youtube = "https://www.youtube.com"
                    )
                ),
                category = "Test",
                tags = listOf("tag1", "tag2")
            )

            _musicPlayerState.update {
                it.copy(currentRadioStations = station)
            }
        }
    }

    fun onAction(action: MusicPlayerAction) {
        when (action) {
            MusicPlayerAction.GetRadioStations -> getRadioStations()
            MusicPlayerAction.PlayMusic -> playMusic()
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
                            it.copy(radioStations = radioStations)
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

    private fun playMusic() {
        // TODO: MODIFY AFTER ADDING YOUTUBE PLAYER
        _musicPlayerState.update {
            it.copy(isMusicPlaying = true)
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