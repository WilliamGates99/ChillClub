package com.xeniac.chillclub.feature_music_player.presensation.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.xeniac.chillclub.R
import com.xeniac.chillclub.core.presentation.utils.toPx
import com.xeniac.chillclub.core.ui.theme.Black
import com.xeniac.chillclub.core.ui.theme.BlackAlpha48
import com.xeniac.chillclub.core.ui.theme.White
import com.xeniac.chillclub.feature_music_player.presensation.MusicPlayerAction
import com.xeniac.chillclub.feature_music_player.presensation.states.MusicPlayerState

@Composable
fun MusicPlayer(
    musicPlayerState: MusicPlayerState,
    modifier: Modifier = Modifier,
    onAction: (action: MusicPlayerAction) -> Unit
) {
    Column(
        modifier = modifier.padding(horizontal = 24.dp)
    ) {
        MusicPlayerTitle(
            musicPlayerState = musicPlayerState,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(24.dp))

        CurrentRadioStation(
            musicPlayerState = musicPlayerState,
            onAction = onAction,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(48.dp))

        ErrorMessage(
            musicPlayerState = musicPlayerState,
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Composable
private fun MusicPlayerTitle(
    musicPlayerState: MusicPlayerState,
    modifier: Modifier = Modifier,
    title: String = if (musicPlayerState.currentRadioStation == null) stringResource(
        id = R.string.music_player_title_not_playing
    ) else musicPlayerState.currentRadioStation.title,
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
private fun ErrorMessage(
    musicPlayerState: MusicPlayerState,
    modifier: Modifier = Modifier,
    errorFontSize: TextUnit = 14.sp,
    errorLineHeight: TextUnit = 18.sp,
    errorLetterSpacing: TextUnit = 0.sp,
    errorFontWeight: FontWeight = FontWeight.Normal,
    errorColor: Color = White,
    errorShadow: Shadow = Shadow(
        color = BlackAlpha48,
        blurRadius = 16.dp.toPx()
    )
) {
    AnimatedVisibility(
        visible = musicPlayerState.errorMessage != null,
        enter = fadeIn(),
        exit = fadeOut(),
        modifier = modifier
    ) {
        Text(
            text = musicPlayerState.errorMessage?.asString() ?: "",
            fontSize = errorFontSize,
            lineHeight = errorLineHeight,
            letterSpacing = errorLetterSpacing,
            fontWeight = errorFontWeight,
            color = errorColor,
            style = TextStyle(
                shadow = errorShadow
            ),
            modifier = modifier
        )
    }
}

@Composable
private fun CurrentRadioStation(
    musicPlayerState: MusicPlayerState,
    modifier: Modifier = Modifier,
    onAction: (action: MusicPlayerAction) -> Unit
) {
    AnimatedVisibility(
        visible = musicPlayerState.currentRadioStation != null,
        enter = fadeIn(),
        exit = fadeOut(),
        modifier = modifier
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(space = 48.dp)) {
            CurrentRadioStationTitle(
                title = musicPlayerState.currentRadioStation!!.channel.name
            )

            MusicPlayerMediaControls(
                musicPlayerState = musicPlayerState,
                onAction = onAction,
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