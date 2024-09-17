package com.xeniac.chillclub.feature_music_player.presensation

import android.graphics.Rect
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.PlayerConstants
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.xeniac.chillclub.core.domain.models.RadioStation
import com.xeniac.chillclub.core.domain.repositories.ConnectivityObserver
import com.xeniac.chillclub.core.domain.utils.Result
import com.xeniac.chillclub.core.presentation.utils.Event
import com.xeniac.chillclub.core.presentation.utils.NetworkObserverHelper
import com.xeniac.chillclub.core.presentation.utils.UiEvent
import com.xeniac.chillclub.feature_music_player.domain.repositories.MusicVolumePercentage
import com.xeniac.chillclub.feature_music_player.domain.use_cases.MusicPlayerUseCases
import com.xeniac.chillclub.feature_music_player.presensation.states.MusicPlayerState
import com.xeniac.chillclub.feature_music_player.presensation.utils.MusicPlayerUiEvent
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
        flow2 = musicPlayerUseCases.getCurrentlyPlayingRadioStationIdUseCase.get()(),
        flow3 = musicPlayerUseCases.observeMusicVolumeChangesUseCase.get()(),
        flow4 = musicPlayerUseCases.getIsPlayInBackgroundEnabledUseCase.get()(),
        flow5 = musicPlayerUseCases.getNotificationPermissionCountUseCase.get()()
    ) { musicPlayerState, currentlyPlayingRadioStationId, musicVolumePercentage, isPlayInBackgroundEnabled, notificationPermissionCount ->
        musicPlayerState.copy(
            currentRadioStationId = currentlyPlayingRadioStationId,
            musicVolumePercentage = musicVolumePercentage,
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

    private val _playMusicEventChannel = Channel<Event>()
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
            MusicPlayerAction.ResumeMusic -> resumeMusic()
            MusicPlayerAction.PauseMusic -> pauseMusic()
            MusicPlayerAction.ShowVolumeSlider -> showVolumeSlider()
            MusicPlayerAction.HideVolumeSlider -> hideVolumeSlider()
            is MusicPlayerAction.SetVolumeSliderBounds -> setVolumeSliderBounds(action.bounds)
            is MusicPlayerAction.AdjustMusicVolume -> adjustMusicVolume(action.newPercentage)
            is MusicPlayerAction.InitializeYouTubePlayer -> initializeYouTubePlayer(action.player)
            is MusicPlayerAction.ShowYouTubePlayerError -> showYouTubePlayerError(action.error)
            is MusicPlayerAction.YouTubePlayerStateChanged -> youTubePlayerStateChanged(action.state)
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
        radiosStation?.id?.let { id ->
            when (val result = musicPlayerUseCases.storeCurrentlyPlayingRadioStationIdUseCase.get()(
                radioStationId = id
            )) {
                is Result.Success -> {
                    _playMusicEventChannel.send(MusicPlayerUiEvent.StartYouTubePlayerService)

                    musicPlayerState.value.youtubePlayer?.loadVideo(
                        videoId = radiosStation.youtubeVideoId,
                        startSeconds = 0f
                    )
                }
                is Result.Error -> {
                    _musicPlayerState.update {
                        it.copy(
                            errorMessage = result.error.asUiText()
                        )
                    }
                }
            }
        }
    }

    private fun resumeMusic() = viewModelScope.launch {
        _playMusicEventChannel.send(MusicPlayerUiEvent.StartYouTubePlayerService)

        musicPlayerState.value.apply {
            if (isMusicCued) {
                musicPlayerState.value.youtubePlayer?.play()
            } else {
                musicPlayerState.value.youtubePlayer?.loadVideo(
                    videoId = currentRadioStation!!.youtubeVideoId,
                    startSeconds = 0f
                )
            }
        }
    }

    private fun pauseMusic() = viewModelScope.launch {
        _playMusicEventChannel.send(MusicPlayerUiEvent.StopYouTubePlayerService)
        musicPlayerState.value.youtubePlayer?.pause()
    }

    private fun showVolumeSlider() = viewModelScope.launch {
        _musicPlayerState.update {
            it.copy(isVolumeSliderVisible = true)
        }
    }

    private fun hideVolumeSlider() = viewModelScope.launch {
        _musicPlayerState.update {
            it.copy(isVolumeSliderVisible = false)
        }
    }

    private fun setVolumeSliderBounds(bounds: Rect) = viewModelScope.launch {
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

    private fun initializeYouTubePlayer(player: YouTubePlayer) = viewModelScope.launch {
        _musicPlayerState.update {
            it.copy(
                youtubePlayer = player,
                errorMessage = null
            )
        }
    }

    private fun showYouTubePlayerError(
        error: PlayerConstants.PlayerError
    ) = viewModelScope.launch {
        _playMusicEventChannel.send(MusicPlayerUiEvent.StopYouTubePlayerService)
        _musicPlayerState.update {
            it.copy(
                isMusicBuffering = false,
                isMusicPlaying = false,
                errorMessage = error.asUiText()
            )
        }
    }

    private fun youTubePlayerStateChanged(
        state: PlayerConstants.PlayerState
    ) = viewModelScope.launch {
        when (state) {
            PlayerConstants.PlayerState.VIDEO_CUED -> {
                _musicPlayerState.update {
                    it.copy(
                        isMusicCued = true,
                        isMusicBuffering = false,
                        isMusicPlaying = false
                    )
                }
            }
            PlayerConstants.PlayerState.UNSTARTED -> {
                _musicPlayerState.update {
                    it.copy(
                        isMusicCued = true,
                        isMusicBuffering = true,
                        isMusicPlaying = false,
                        errorMessage = null
                    )
                }
            }
            PlayerConstants.PlayerState.BUFFERING -> {
                _musicPlayerState.update {
                    it.copy(
                        isMusicBuffering = false,
                        isMusicPlaying = false,
                        errorMessage = null
                    )
                }
            }
            PlayerConstants.PlayerState.PLAYING -> {
                _musicPlayerState.update {
                    it.copy(
                        isMusicBuffering = false,
                        isMusicPlaying = true,
                        errorMessage = null
                    )
                }
            }
            PlayerConstants.PlayerState.PAUSED -> {
                _musicPlayerState.update {
                    it.copy(
                        isMusicBuffering = false,
                        isMusicPlaying = false
                    )
                }
            }
            PlayerConstants.PlayerState.ENDED -> {
                _musicPlayerState.update {
                    it.copy(
                        isMusicBuffering = false,
                        isMusicPlaying = false
                    )
                }
            }
            PlayerConstants.PlayerState.UNKNOWN -> {
                _musicPlayerState.update {
                    it.copy(
                        isMusicBuffering = false,
                        isMusicPlaying = false
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