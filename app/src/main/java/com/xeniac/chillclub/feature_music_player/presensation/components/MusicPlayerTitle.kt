package com.xeniac.chillclub.feature_music_player.presensation.components

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.sp
import com.xeniac.chillclub.R
import com.xeniac.chillclub.core.ui.theme.White
import com.xeniac.chillclub.feature_music_player.presensation.states.MusicPlayerState

@Composable
fun MusicPlayerTitle(
    musicPlayerState: MusicPlayerState,
    modifier: Modifier = Modifier,
    title: String = if (musicPlayerState.currentRadioStations == null) stringResource(
        id = R.string.music_player_title_not_playing
    ) else musicPlayerState.currentRadioStations.title,
    titleFontSize: TextUnit = 55.sp,
    titleLineHeight: TextUnit = 55.sp,
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