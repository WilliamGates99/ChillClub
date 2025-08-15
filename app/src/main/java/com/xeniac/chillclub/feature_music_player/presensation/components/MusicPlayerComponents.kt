package com.xeniac.chillclub.feature_music_player.presensation.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.LocalTextStyle
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.xeniac.chillclub.R
import com.xeniac.chillclub.core.domain.models.RadioStation
import com.xeniac.chillclub.core.presentation.common.ui.theme.Black
import com.xeniac.chillclub.core.presentation.common.ui.theme.BlackAlpha48
import com.xeniac.chillclub.core.presentation.common.ui.theme.White
import com.xeniac.chillclub.core.presentation.common.ui.utils.toPx
import com.xeniac.chillclub.core.presentation.common.utils.UiText
import com.xeniac.chillclub.feature_music_player.presensation.MusicPlayerAction
import com.xeniac.chillclub.feature_music_player.presensation.states.MusicPlayerState

@Composable
fun MusicPlayer(
    state: MusicPlayerState,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(
        horizontal = 24.dp,
        vertical = 8.dp
    ),
    onAction: (action: MusicPlayerAction) -> Unit
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(contentPadding)
    ) {
        MusicPlayerTitle(
            currentRadioStation = state.currentRadioStation
        )

        Spacer(modifier = Modifier.height(24.dp))

        CurrentRadioStation(
            state = state,
            onAction = onAction
        )

        Spacer(modifier = Modifier.height(48.dp))

        ErrorMessage(errorMessage = state.errorMessage)
    }
}

@Composable
private fun MusicPlayerTitle(
    currentRadioStation: RadioStation?,
    modifier: Modifier = Modifier,
    title: String = currentRadioStation?.title ?: stringResource(
        id = R.string.music_player_title_not_playing
    ),
    textStyle: TextStyle = LocalTextStyle.current.copy(
        fontSize = 56.sp,
        lineHeight = 60.sp,
        letterSpacing = 0.sp,
        fontWeight = FontWeight.Black,
        color = White
    )
) {
    Text(
        text = title,
        style = textStyle,
        modifier = modifier.fillMaxWidth()
    )
}

@Composable
private fun ErrorMessage(
    errorMessage: UiText?,
    modifier: Modifier = Modifier,
    textStyle: TextStyle = LocalTextStyle.current.copy(
        fontSize = 14.sp,
        lineHeight = 18.sp,
        letterSpacing = 0.sp,
        fontWeight = FontWeight.Normal,
        color = White,
        shadow = Shadow(
            color = BlackAlpha48,
            blurRadius = 16.dp.toPx()
        )
    )
) {
    AnimatedVisibility(
        visible = errorMessage != null,
        enter = fadeIn(),
        exit = fadeOut(),
        modifier = modifier
    ) {
        Text(
            text = errorMessage?.asString().orEmpty(),
            style = textStyle,
            modifier = modifier.fillMaxWidth()
        )
    }
}

@Composable
private fun CurrentRadioStation(
    state: MusicPlayerState,
    modifier: Modifier = Modifier,
    onAction: (action: MusicPlayerAction) -> Unit
) {
    AnimatedVisibility(
        visible = state.currentRadioStation != null,
        enter = fadeIn(),
        exit = fadeOut(),
        modifier = modifier.fillMaxWidth()
    ) {
        state.currentRadioStation?.let { currentRadioStation ->
            Column(
                verticalArrangement = Arrangement.spacedBy(space = 48.dp),
                modifier = modifier.fillMaxWidth()
            ) {
                CurrentRadioStationTitle(title = currentRadioStation.channel.name)

                MusicPlayerMediaControls(
                    state = state,
                    onAction = onAction
                )
            }
        }
    }
}

@Composable
private fun CurrentRadioStationTitle(
    title: String,
    modifier: Modifier = Modifier,
    background: Color = Black,
    shape: Shape = RectangleShape,
    contentPadding: PaddingValues = PaddingValues(
        horizontal = 14.dp,
        vertical = 4.dp
    ),
    textStyle: TextStyle = LocalTextStyle.current.copy(
        fontSize = 16.sp,
        lineHeight = 16.sp,
        fontWeight = FontWeight.Normal,
        textAlign = TextAlign.Center,
        color = White
    ),
    maxLines: Int = 1,
    textOverflow: TextOverflow = TextOverflow.Ellipsis
) {
    Text(
        text = title,
        style = textStyle,
        maxLines = maxLines,
        overflow = textOverflow,
        modifier = modifier
            .clip(shape)
            .background(background)
            .padding(contentPadding)
    )
}