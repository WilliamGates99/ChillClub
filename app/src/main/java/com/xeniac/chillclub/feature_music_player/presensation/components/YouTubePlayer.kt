package com.xeniac.chillclub.feature_music_player.presensation.components

import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.PlayerConstants
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView
import com.xeniac.chillclub.feature_music_player.presensation.MusicPlayerAction

@Composable
fun YouTubePlayer(
    isPlayInBackgroundEnabled: Boolean?,
    modifier: Modifier = Modifier,
    onAction: (action: MusicPlayerAction) -> Unit
) {
    var shouldCreateYouTubePlayer by remember { mutableStateOf(false) }

    LaunchedEffect(key1 = isPlayInBackgroundEnabled) {
        shouldCreateYouTubePlayer = isPlayInBackgroundEnabled != null
    }

    if (shouldCreateYouTubePlayer) {
        AndroidView(
            factory = { context ->
                YouTubePlayerView(
                    context = context
                ).apply {
                    enableBackgroundPlayback(enable = isPlayInBackgroundEnabled ?: true)

                    addYouTubePlayerListener(
                        object : AbstractYouTubePlayerListener() {
                            override fun onReady(youTubePlayer: YouTubePlayer) {
                                super.onReady(youTubePlayer)
                                onAction(
                                    MusicPlayerAction.InitializeYouTubePlayer(player = youTubePlayer)
                                )
                            }

                            override fun onError(
                                youTubePlayer: YouTubePlayer,
                                error: PlayerConstants.PlayerError
                            ) {
                                super.onError(youTubePlayer, error)
                                onAction(MusicPlayerAction.ShowYouTubePlayerError(error = error))
                            }

                            override fun onStateChange(
                                youTubePlayer: YouTubePlayer,
                                state: PlayerConstants.PlayerState
                            ) {
                                super.onStateChange(youTubePlayer, state)
                                onAction(MusicPlayerAction.YouTubePlayerStateChanged(state = state))
                            }
                        }
                    )
                }
            },
            update = { playerView ->
                playerView.enableBackgroundPlayback(enable = isPlayInBackgroundEnabled ?: true)
            },
            onRelease = { playerView ->
                playerView.release()
            },
            modifier = modifier.size(0.dp) // 0.dp size to hide the player
        )
    }
}