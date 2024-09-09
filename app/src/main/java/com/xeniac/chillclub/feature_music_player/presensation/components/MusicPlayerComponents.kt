package com.xeniac.chillclub.feature_music_player.presensation.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.xeniac.chillclub.R
import com.xeniac.chillclub.core.ui.theme.Black
import com.xeniac.chillclub.core.ui.theme.White
import com.xeniac.chillclub.feature_music_player.presensation.states.MusicPlayerState

@Composable
fun MusicPlayer(
    musicPlayerState: MusicPlayerState,
    modifier: Modifier = Modifier,
    onPlayClick: () -> Unit,
    onPauseClick: () -> Unit,
    showVolumeSlider: () -> Unit,
    hideVolumeSlider: () -> Unit,
    onVolumeSliderShown: (volumeSliderBounds: Rect) -> Unit,
    adjustMusicVolume: (newPercentage: Float) -> Unit
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(space = 24.dp),
        modifier = modifier.padding(horizontal = 24.dp)
    ) {
        MusicPlayerTitle(
            musicPlayerState = musicPlayerState,
            modifier = Modifier.fillMaxWidth()
        )

        CurrentRadioStation(
            musicPlayerState = musicPlayerState,
            onPlayClick = onPlayClick,
            onPauseClick = onPauseClick,
            showVolumeSlider = showVolumeSlider,
            hideVolumeSlider = hideVolumeSlider,
            onVolumeSliderShown = onVolumeSliderShown,
            adjustMusicVolume = adjustMusicVolume,
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
        )
    }
}

@Composable
private fun MusicPlayerTitle(
    musicPlayerState: MusicPlayerState,
    modifier: Modifier = Modifier,
    title: String = if (musicPlayerState.currentRadioStations == null) stringResource(
        id = R.string.music_player_title_not_playing
    ) else musicPlayerState.currentRadioStations.title,
    titleFontSize: TextUnit = 56.sp,
    titleLineHeight: TextUnit = 60.sp,
    titleLetterSpacing: TextUnit = 0.sp,
    titleFontWeight: FontWeight = FontWeight.Black,
    titleColor: Color = White
) {
    Text(
        text = title,
        fontSize = titleFontSize,
        lineHeight = titleLineHeight,
        letterSpacing = titleLetterSpacing,
        fontWeight = titleFontWeight,
        color = titleColor,
        modifier = modifier
    )
}

@Composable
private fun CurrentRadioStation(
    musicPlayerState: MusicPlayerState,
    modifier: Modifier = Modifier,
    onPlayClick: () -> Unit,
    onPauseClick: () -> Unit,
    showVolumeSlider: () -> Unit,
    hideVolumeSlider: () -> Unit,
    onVolumeSliderShown: (volumeSliderBounds: Rect) -> Unit,
    adjustMusicVolume: (newPercentage: Float) -> Unit
) {
    AnimatedVisibility(
        visible = musicPlayerState.currentRadioStations != null,
        enter = fadeIn(),
        exit = fadeOut(),
        modifier = modifier
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(space = 48.dp),
            modifier = Modifier.fillMaxSize()
        ) {
            CurrentRadioStationTitle(
                title = musicPlayerState.currentRadioStations!!.channel.name
            )

            MusicPlayerMediaControls(
                musicPlayerState = musicPlayerState,
                onPlayClick = onPlayClick,
                onPauseClick = onPauseClick,
                showVolumeSlider = showVolumeSlider,
                hideVolumeSlider = hideVolumeSlider,
                onVolumeSliderShown = onVolumeSliderShown,
                adjustMusicVolume = adjustMusicVolume,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

@Composable
private fun CurrentRadioStationTitle(
    title: String,
    modifier: Modifier = Modifier,
    background: Color = Black,
    shape: Shape = RectangleShape,
    titleFontSize: TextUnit = 16.sp,
    titleLineHeight: TextUnit = 16.sp,
    titleFontWeight: FontWeight = FontWeight.Normal,
    titleTextAlign: TextAlign = TextAlign.Center,
    titleMaxLines: Int = 1,
    titleOverflow: TextOverflow = TextOverflow.Ellipsis,
    titleColor: Color = White
) {
    Text(
        text = title,
        fontSize = titleFontSize,
        lineHeight = titleLineHeight,
        fontWeight = titleFontWeight,
        textAlign = titleTextAlign,
        maxLines = titleMaxLines,
        overflow = titleOverflow,
        color = titleColor,
        modifier = modifier
            .clip(shape)
            .background(background)
            .padding(
                horizontal = 14.dp,
                vertical = 4.dp
            )
    )
}