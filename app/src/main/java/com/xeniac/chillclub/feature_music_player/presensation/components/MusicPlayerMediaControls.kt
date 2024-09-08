package com.xeniac.chillclub.feature_music_player.presensation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.boundsInWindow
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.popup
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.ConstraintSet
import androidx.constraintlayout.compose.Dimension
import androidx.constraintlayout.compose.Visibility
import com.xeniac.chillclub.R
import com.xeniac.chillclub.core.ui.components.CustomIconButton
import com.xeniac.chillclub.feature_music_player.domain.repositories.MusicVolumePercentage
import com.xeniac.chillclub.feature_music_player.presensation.states.MusicPlayerState

@Composable
fun MusicPlayerMediaControls(
    musicPlayerState: MusicPlayerState,
    modifier: Modifier = Modifier,
    onVolumeSliderShown: (volumeSliderBounds: Rect) -> Unit,
    showVolumeSlider: () -> Unit,
    hideVolumeSlider: () -> Unit,
    onDecreaseVolume: () -> Unit,
    onIncreaseVolume: () -> Unit,
    onPlayClick: () -> Unit,
    onPauseClick: () -> Unit
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
            onVolumeSliderShown = onVolumeSliderShown,
            showVolumeSlider = showVolumeSlider,
            hideVolumeSlider = hideVolumeSlider,
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
    onVolumeSliderShown: (volumeSliderBounds: Rect) -> Unit,
    showVolumeSlider: () -> Unit,
    hideVolumeSlider: () -> Unit,
    onClick: () -> Unit // TODO: CHANGE TO ON_SLIDER_CHANGED
) {
    val constraintSet = ConstraintSet {
        val volumeBtn = createRefFor(id = "volumeBtn")
        val volumeSlider = createRefFor(id = "volumeSlider")

        constrain(ref = volumeBtn) {
            start.linkTo(anchor = parent.start)
            end.linkTo(anchor = parent.end)
            top.linkTo(anchor = parent.top)
            bottom.linkTo(anchor = parent.bottom)
        }

        constrain(ref = volumeSlider) {
            start.linkTo(anchor = parent.start)
            end.linkTo(anchor = parent.end)
            bottom.linkTo(anchor = parent.top, margin = (-28).dp)

            horizontalChainWeight = 0.5f

            width = Dimension.value(36.dp)
            height = Dimension.value(120.dp)

            visibility = if (musicPlayerState.isVolumeSliderVisible) {
                Visibility.Visible
            } else Visibility.Gone
        }
    }

    ConstraintLayout(
        constraintSet = constraintSet,
        modifier = modifier
    ) {
        Box(
            modifier = Modifier
                .layoutId("volumeSlider")
                .semantics { this.popup() }
                .width(36.dp)
                .height(120.dp)
                .clip(RoundedCornerShape(44.dp))
                .background(Color.Yellow)
                .onGloballyPositioned { coordinates ->
                    onVolumeSliderShown(coordinates.boundsInWindow())
                }
        ) {
            // TODO: IMPLEMENT VOLUME SLIDER
        }

        CustomIconButton(
            icon = icon,
            contentDescription = contentDescription,
            onClick = {
                if (musicPlayerState.isVolumeSliderVisible) {
                    hideVolumeSlider()
                } else showVolumeSlider()
            },
            modifier = Modifier.layoutId("volumeBtn")
        )
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