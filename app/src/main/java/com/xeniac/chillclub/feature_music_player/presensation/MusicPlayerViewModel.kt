package com.xeniac.chillclub.feature_music_player.presensation

import android.graphics.Rect
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.PlayerConstants
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.xeniac.chillclub.core.domain.models.RadioStation
import com.xeniac.chillclub.core.domain.models.Result
import com.xeniac.chillclub.core.presentation.common.utils.Event
import com.xeniac.chillclub.core.presentation.common.utils.NetworkObserverHelper.hasNetworkConnection
import com.xeniac.chillclub.core.presentation.common.utils.UiEvent
import com.xeniac.chillclub.feature_music_player.domain.repositories.MusicVolumePercentage
import com.xeniac.chillclub.feature_music_player.domain.use_cases.MusicPlayerUseCases
import com.xeniac.chillclub.feature_music_player.presensation.states.MusicPlayerState
import com.xeniac.chillclub.feature_music_player.presensation.utils.MusicPlayerUiEvent
import com.xeniac.chillclub.feature_music_player.presensation.utils.asUiText
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChangedBy
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MusicPlayerViewModel @Inject constructor(
    private val musicPlayerUseCases: MusicPlayerUseCases
) : ViewModel() {

    private val _state = MutableStateFlow(MusicPlayerState())
    val state = combine(
        flow = _state,
        flow2 = musicPlayerUseCases.getCurrentlyPlayingRadioStationIdUseCase.get()(),
        flow3 = musicPlayerUseCases.observeMusicVolumeChangesUseCase.get()(),
        flow4 = musicPlayerUseCases.getIsPlayInBackgroundEnabledUseCase.get()(),
        flow5 = musicPlayerUseCases.getNotificationPermissionCountUseCase.get()()
    ) { state, currentlyPlayingRadioStationId, musicVolumePercentage, isPlayInBackgroundEnabled, notificationPermissionCount ->
        state.copy(
            currentRadioStationId = currentlyPlayingRadioStationId,
            musicVolumePercentage = musicVolumePercentage,
            isPlayInBackgroundEnabled = isPlayInBackgroundEnabled,
            notificationPermissionCount = notificationPermissionCount
        )
    }.onStart {
        observeIsPlayInBackgroundEnabled()
        observeCurrentRadioStationId()
        getRadioStations()
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.Eagerly,
        initialValue = _state.value
    )

    private val _playMusicEventChannel = Channel<Event>()
    val playMusicEventChannel = _playMusicEventChannel.receiveAsFlow()

    private val _adjustMusicVolumeEventChannel = Channel<UiEvent>()
    val adjustMusicVolumeEventChannel = _adjustMusicVolumeEventChannel.receiveAsFlow()

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

    @OptIn(ExperimentalCoroutinesApi::class)
    private fun observeIsPlayInBackgroundEnabled() {
        _state.distinctUntilChangedBy {
            it.isPlayInBackgroundEnabled
        }.mapLatest {
            it.isPlayInBackgroundEnabled != null
        }.onEach { shouldCreateYouTubePlayer ->
            _state.update {
                it.copy(shouldCreateYouTubePlayer = shouldCreateYouTubePlayer)
            }
        }.launchIn(scope = viewModelScope)
    }

    private fun observeCurrentRadioStationId() {
        _state.distinctUntilChangedBy {
            it.currentRadioStationId
        }.onEach {
            getCurrentRadioStation()
        }.launchIn(scope = viewModelScope)
    }

    private fun getRadioStations() {
        musicPlayerUseCases.getRadioStationsUseCase.get()(
            fetchFromRemote = hasNetworkConnection()
        ).onStart {
            _state.update {
                it.copy(isRadioStationsLoading = true)
            }
        }.onEach { result ->
            when (result) {
                is Result.Success -> {
                    _state.update {
                        it.copy(
                            radioStations = result.data,
                            isRadioStationsLoading = false
                        )
                    }
                }
                is Result.Error -> {
                    _state.update {
                        it.copy(errorMessage = result.error.asUiText())
                    }
                }
            }
        }.onCompletion { throwable ->
            if (throwable == null) {
                _state.update {
                    it.copy(isRadioStationsLoading = false)
                }
            }

            if (throwable is CancellationException) {
                _state.update {
                    it.copy(isRadioStationsLoading = false)
                }
            }
        }.launchIn(scope = viewModelScope)
    }

    private fun getCurrentRadioStation() {
        musicPlayerUseCases.getCurrentlyPlayingRadioStationUseCase.get()(
            radioStationId = _state.value.currentRadioStationId
        ).onEach { radioStation ->
            _state.update {
                it.copy(currentRadioStation = radioStation)
            }
        }.launchIn(scope = viewModelScope)
    }

    private fun playMusic(
        radiosStation: RadioStation?
    ) {
        radiosStation?.id?.let { id ->
            musicPlayerUseCases.storeCurrentlyPlayingRadioStationIdUseCase.get()(
                radioStationId = id
            ).onEach { result ->
                when (result) {
                    is Result.Success -> {
                        _playMusicEventChannel.send(MusicPlayerUiEvent.StartYouTubePlayerService)

                        _state.value.youtubePlayer?.loadVideo(
                            videoId = radiosStation.youtubeVideoId,
                            startSeconds = 0f
                        )
                    }
                    is Result.Error -> {
                        _state.update {
                            it.copy(errorMessage = result.error.asUiText())
                        }
                    }
                }
            }.launchIn(scope = viewModelScope)
        }
    }

    private fun resumeMusic() = viewModelScope.launch {
        _playMusicEventChannel.send(MusicPlayerUiEvent.StartYouTubePlayerService)

        with(_state.value) {
            when {
                isMusicCued -> youtubePlayer?.play()
                else -> youtubePlayer?.loadVideo(
                    videoId = currentRadioStation?.youtubeVideoId.orEmpty(),
                    startSeconds = 0f
                )
            }
        }
    }

    private fun pauseMusic() = viewModelScope.launch {
        _playMusicEventChannel.send(MusicPlayerUiEvent.StopYouTubePlayerService)
        _state.value.youtubePlayer?.pause()
    }

    private fun showVolumeSlider() = viewModelScope.launch {
        _state.update {
            it.copy(isVolumeSliderVisible = true)
        }
    }

    private fun hideVolumeSlider() = viewModelScope.launch {
        _state.update {
            it.copy(isVolumeSliderVisible = false)
        }
    }

    private fun setVolumeSliderBounds(
        bounds: Rect
    ) = viewModelScope.launch {
        _state.update {
            it.copy(volumeSliderBounds = bounds)
        }
    }

    private fun adjustMusicVolume(
        newPercentage: MusicVolumePercentage
    ) {
        musicPlayerUseCases.adjustMusicVolumeUseCase.get()(
            newPercentage = newPercentage,
            currentPercentage = _state.value.musicVolumePercentage
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

    private fun initializeYouTubePlayer(
        player: YouTubePlayer
    ) = viewModelScope.launch {
        _state.update {
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
        _state.update {
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
                _state.update {
                    it.copy(
                        isMusicCued = true,
                        isMusicBuffering = false,
                        isMusicPlaying = false
                    )
                }
            }
            PlayerConstants.PlayerState.UNSTARTED -> {
                _state.update {
                    it.copy(
                        isMusicCued = true,
                        isMusicBuffering = true,
                        isMusicPlaying = false,
                        errorMessage = null
                    )
                }
            }
            PlayerConstants.PlayerState.BUFFERING -> {
                _state.update {
                    it.copy(
                        isMusicBuffering = false,
                        isMusicPlaying = false,
                        errorMessage = null
                    )
                }
            }
            PlayerConstants.PlayerState.PLAYING -> {
                _state.update {
                    it.copy(
                        isMusicBuffering = false,
                        isMusicPlaying = true,
                        errorMessage = null
                    )
                }
            }
            PlayerConstants.PlayerState.PAUSED -> {
                _state.update {
                    it.copy(
                        isMusicBuffering = false,
                        isMusicPlaying = false
                    )
                }
            }
            PlayerConstants.PlayerState.ENDED -> {
                _state.update {
                    it.copy(
                        isMusicBuffering = false,
                        isMusicPlaying = false
                    )
                }
            }
            PlayerConstants.PlayerState.UNKNOWN -> {
                _state.update {
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
        val shouldAskForPermission = _state.value.run {
            notificationPermissionCount < 1 && !permissionDialogQueue.contains(permission) && !isGranted
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
        musicPlayerUseCases.storeNotificationPermissionCountUseCase.get()(
            count = _state.value.notificationPermissionCount.plus(other = 1)
        )

        _state.update {
            it.copy(
                permissionDialogQueue = _state.value.permissionDialogQueue.toMutableList().apply {
                    remove(permission)
                },
                isPermissionDialogVisible = false
            )
        }
    }
}