package com.xeniac.chillclub.feature_music_player.presensation.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SliderDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.boundsInWindow
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.popup
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.ConstraintSet
import androidx.constraintlayout.compose.Dimension
import com.xeniac.chillclub.R
import com.xeniac.chillclub.core.ui.components.CustomIconButton
import com.xeniac.chillclub.core.ui.components.VerticalSlider
import com.xeniac.chillclub.feature_music_player.domain.repositories.MusicVolumePercentage
import com.xeniac.chillclub.feature_music_player.presensation.MusicPlayerAction
import com.xeniac.chillclub.feature_music_player.presensation.states.MusicPlayerState

@Composable
fun MusicPlayerMediaControls(
    musicPlayerState: MusicPlayerState,
    modifier: Modifier = Modifier,
    onAction: (action: MusicPlayerAction) -> Unit
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
            onAction = onAction
        )

        VolumeControlButton(
            musicPlayerState = musicPlayerState,
            onAction = onAction
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
    onAction: (action: MusicPlayerAction) -> Unit
) {
    CustomIconButton(
        isLoading = musicPlayerState.isMusicBuffering || musicPlayerState.youtubePlayer == null,
        icon = icon,
        contentDescription = contentDescription,
        onClick = {
            if (musicPlayerState.isMusicPlaying) {
                onAction(MusicPlayerAction.PauseMusic)
            } else onAction(MusicPlayerAction.ResumeMusic)
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
    volumeSliderWidth: Dp = 48.dp,
    volumeSliderHeight: Dp = 148.dp,
    onAction: (action: MusicPlayerAction) -> Unit
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
            bottom.linkTo(anchor = parent.top, margin = (-36).dp)

            horizontalChainWeight = 0.5f

            width = Dimension.value(volumeSliderWidth)
            height = Dimension.value(volumeSliderHeight)
        }
    }

    ConstraintLayout(
        constraintSet = constraintSet,
        modifier = modifier
    ) {
        AnimatedVisibility(
            visible = musicPlayerState.isVolumeSliderVisible,
            enter = scaleIn(
                animationSpec = spring(stiffness = Spring.StiffnessMediumLow),
                transformOrigin = TransformOrigin(pivotFractionX = 0.5f, pivotFractionY = 1f)
            ),
            exit = scaleOut(
                animationSpec = spring(stiffness = Spring.StiffnessMedium),
                transformOrigin = TransformOrigin(pivotFractionX = 0.5f, pivotFractionY = 1f)
            ),
            modifier = Modifier.layoutId("volumeSlider")
        ) {
            VolumeSlider(
                musicPlayerState = musicPlayerState,
                sliderWidth = volumeSliderWidth,
                sliderHeight = volumeSliderHeight,
                onAction = onAction,
                modifier = Modifier.fillMaxSize()
            )
        }

        CustomIconButton(
            icon = icon,
            contentDescription = contentDescription,
            onClick = {
                if (musicPlayerState.isVolumeSliderVisible) {
                    onAction(MusicPlayerAction.HideVolumeSlider)
                } else onAction(MusicPlayerAction.ShowVolumeSlider)
            },
            modifier = Modifier.layoutId("volumeBtn")
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VolumeSlider(
    musicPlayerState: MusicPlayerState,
    sliderWidth: Dp,
    sliderHeight: Dp,
    modifier: Modifier = Modifier,
    volumeSliderShape: Shape = RoundedCornerShape(90.dp),
    volumeSliderBackground: Color = MaterialTheme.colorScheme.surface,
    onAction: (action: MusicPlayerAction) -> Unit
) {
    var startVolumeSliderAnimation by remember { mutableStateOf(false) }
    val animatedVolumeSliderPosition by animateFloatAsState(
        targetValue = if (startVolumeSliderAnimation) {
            musicPlayerState.musicVolumePercentage
        } else 0f,
        animationSpec = spring(stiffness = Spring.StiffnessLow),
        label = "animatedVolumeSliderPosition"
    )

    LaunchedEffect(key1 = Unit) {
        startVolumeSliderAnimation = true
    }

    Box(
        modifier = modifier
            .semantics { this.popup() }
            .clip(volumeSliderShape)
            .background(volumeSliderBackground)
            .onGloballyPositioned { coordinates ->
                val bounds = coordinates.boundsInWindow()
                onAction(
                    MusicPlayerAction.SetVolumeSliderBounds(
                        android.graphics.Rect(
                            /* left = */ bounds.left.toInt(),
                            /* top = */ bounds.top.toInt(),
                            /* right = */ bounds.right.toInt(),
                            /* bottom = */ bounds.bottom.toInt()
                        )
                    )
                )
            }
    ) {
        val interactionSource = remember { MutableInteractionSource() }

        VerticalSlider(
            value = animatedVolumeSliderPosition,
            onValueChange = { newPercentage ->
                onAction(MusicPlayerAction.AdjustMusicVolume(newPercentage))
            },
            paddingValues = PaddingValues(
                start = 42.dp,
                end = 8.dp
            ),
            width = sliderWidth,
            height = sliderHeight,
            interactionSource = interactionSource,
            thumb = {
                SliderDefaults.Thumb(
                    interactionSource = interactionSource,
                    thumbSize = DpSize(
                        width = 16.dp,
                        height = 16.dp
                    ),
                    modifier = Modifier.align(Alignment.Center)
                )
            },
            track = { sliderState ->
                SliderDefaults.Track(
                    sliderState = sliderState,
                    drawStopIndicator = null,
                    thumbTrackGapSize = 0.dp,
                    trackInsideCornerSize = 0.dp,
                    modifier = Modifier.height(4.dp)
                )
            }
        )
    }
}

@Composable
private fun MusicVolumePercentage.getVolumeIcon(): Painter = when {
    this > 0.8 -> painterResource(R.drawable.ic_music_player_volume_full)
    this > 0.4 -> painterResource(R.drawable.ic_music_player_volume_mid)
    this > 0 -> painterResource(R.drawable.ic_music_player_volume_low)
    else -> painterResource(R.drawable.ic_music_player_volume_mute)
}