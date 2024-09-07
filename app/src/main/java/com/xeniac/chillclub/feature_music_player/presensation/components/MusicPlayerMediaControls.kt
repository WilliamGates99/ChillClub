package com.xeniac.chillclub.feature_music_player.presensation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Popup
import androidx.compose.ui.window.PopupProperties
import com.xeniac.chillclub.R
import com.xeniac.chillclub.core.ui.components.CustomIconButton
import com.xeniac.chillclub.feature_music_player.domain.repositories.MusicVolumePercentage
import com.xeniac.chillclub.feature_music_player.presensation.states.MusicPlayerState

@Composable
fun MusicPlayerMediaControls(
    musicPlayerState: MusicPlayerState,
    modifier: Modifier = Modifier,
    onPlayClick: () -> Unit,
    onPauseClick: () -> Unit,
    onIncreaseVolume: () -> Unit,
    onDecreaseVolume: () -> Unit
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(
            space = 8.dp,
            alignment = Alignment.Start
        ),
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
    ) {
        PlayPauseButton(
            musicPlayerState = musicPlayerState,
            onPlayClick = onPlayClick,
            onPauseClick = onPauseClick
        )

        VolumeControlButton(
            musicPlayerState = musicPlayerState,
            onClick = onDecreaseVolume // TODO: CHANGE
        )
    }
}

@Composable
fun PlayPauseButton(
    musicPlayerState: MusicPlayerState,
    modifier: Modifier = Modifier,
    icon: Painter = if (musicPlayerState.isMusicPlaying) painterResource(
        R.drawable.ic_music_player_pause
    ) else painterResource(R.drawable.ic_music_player_play),
    contentDescription: String = if (musicPlayerState.isMusicPlaying) stringResource(
        id = R.string.music_player_btn_pause
    ) else stringResource(id = R.string.music_player_btn_play),
    onPlayClick: () -> Unit,
    onPauseClick: () -> Unit
) {
    CustomIconButton(
        icon = icon,
        contentDescription = contentDescription,
        onClick = {
            if (musicPlayerState.isMusicPlaying) onPauseClick() else onPlayClick()
        },
        modifier = modifier
    )
}

@Composable
fun VolumeControlButton(
    musicPlayerState: MusicPlayerState,
    modifier: Modifier = Modifier,
    icon: Painter = musicPlayerState.musicVolumePercentage.getVolumeIcon(),
    contentDescription: String = stringResource(id = R.string.music_player_btn_volume_control),
    onClick: () -> Unit
) {
    Box(
        modifier = modifier
    ) {
        var isPopupVisible by remember { mutableStateOf(false) }

        CustomIconButton(
            icon = icon,
            contentDescription = contentDescription,
            onClick = {
                isPopupVisible = true
            }
        )

        // TODO: USE CONSTRAINT LAYOUT AND A BOX INSTEAD OF THE POPUP. BECAUSE POPUP ALWAYS SHOWS ON TOP OF OTHER CONTENTS
        if (isPopupVisible) {
            Popup(
                alignment = Alignment.BottomCenter,
//                offset = IntOffset(x = 0, y = -popupSizePx),
                properties = PopupProperties(
                    dismissOnClickOutside = true,
                    dismissOnBackPress = true,
                    excludeFromSystemGesture = true
                ),
                onDismissRequest = {
                    isPopupVisible = false
                }
            ) {
                Box(
                    Modifier
                        .width(36.dp)
                        .height(120.dp)
                        .clip(RoundedCornerShape(44.dp))
                        .background(Color.Yellow)
                )
            }
        }
    }
}

@Composable
private fun MusicVolumePercentage?.getVolumeIcon(): Painter = when {
    this == null -> painterResource(R.drawable.ic_music_player_volume_mute)
    this > 80 -> painterResource(R.drawable.ic_music_player_volume_full)
    this > 40 -> painterResource(R.drawable.ic_music_player_volume_mid)
    this > 0 -> painterResource(R.drawable.ic_music_player_volume_mid)
    else -> painterResource(R.drawable.ic_music_player_volume_mute)
}