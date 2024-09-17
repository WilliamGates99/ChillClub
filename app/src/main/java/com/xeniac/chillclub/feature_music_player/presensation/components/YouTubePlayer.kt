package com.xeniac.chillclub.feature_music_player.presensation.components

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.PlayerConstants
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView
import com.xeniac.chillclub.feature_music_player.presensation.MusicPlayerAction
import com.xeniac.chillclub.feature_music_player.presensation.states.MusicPlayerState

@Composable
fun YouTubePlayer(
    musicPlayerState: MusicPlayerState,
    modifier: Modifier = Modifier,
    onAction: (action: MusicPlayerAction) -> Unit
) {
    var shouldCreateYouTubePlayer by remember { mutableStateOf(false) }

    LaunchedEffect(musicPlayerState.isPlayInBackgroundEnabled) {
        shouldCreateYouTubePlayer = musicPlayerState.isPlayInBackgroundEnabled != null
    }

    if (shouldCreateYouTubePlayer) {
        AndroidView(
            factory = { context ->
                YouTubePlayerView(context).apply {
                    enableBackgroundPlayback(enable = musicPlayerState.isPlayInBackgroundEnabled!!)

                    addYouTubePlayerListener(object : AbstractYouTubePlayerListener() {
                        override fun onReady(youTubePlayer: YouTubePlayer) {
                            super.onReady(youTubePlayer)
                            onAction(MusicPlayerAction.InitializeYouTubePlayer(youTubePlayer))
                        }

                        override fun onError(
                            youTubePlayer: YouTubePlayer,
                            error: PlayerConstants.PlayerError
                        ) {
                            super.onError(youTubePlayer, error)
                            onAction(MusicPlayerAction.ShowYouTubePlayerError(error))
                        }

                        override fun onStateChange(
                            youTubePlayer: YouTubePlayer,
                            state: PlayerConstants.PlayerState
                        ) {
                            super.onStateChange(youTubePlayer, state)
                            onAction(MusicPlayerAction.YouTubePlayerStateChanged(state))
                        }
                    })
                }
            },
            onRelease = { playerView ->
                playerView.release()
            },
            modifier = modifier
        )
    }
}