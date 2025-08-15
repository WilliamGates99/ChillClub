@file:OptIn(ExperimentalMaterial3Api::class)

package com.xeniac.chillclub.feature_music_player.presensation.components

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SheetState
import androidx.compose.material3.SheetValue
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.fromHtml
import androidx.compose.ui.text.toUpperCase
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.PlayerConstants
import com.xeniac.chillclub.R
import com.xeniac.chillclub.core.domain.models.RadioStation
import com.xeniac.chillclub.core.presentation.common.ui.utils.getNavigationBarHeight
import com.xeniac.chillclub.core.presentation.common.ui.utils.getNavigationBarHeightDp
import com.xeniac.chillclub.feature_music_player.presensation.MusicPlayerAction
import com.xeniac.chillclub.feature_music_player.presensation.states.MusicPlayerState
import com.xeniac.chillclub.feature_music_player.presensation.utils.TestTags
import kotlinx.coroutines.launch

@Composable
fun RadioStationsBottomSheet(
    state: MusicPlayerState,
    sheetState: SheetState,
    modifier: Modifier = Modifier,
    navigationBarHeight: Dp = getNavigationBarHeightDp(),
    onHeaderHeightCalculated: (heightPx: Int) -> Unit,
    onHeaderClick: () -> Unit,
    onAction: (action: MusicPlayerAction) -> Unit
) {
    val scope = rememberCoroutineScope()

    Column(modifier = modifier.fillMaxWidth()) {
        BottomSheetHeader(
            sheetState = sheetState,
            onHeaderHeightCalculated = onHeaderHeightCalculated,
            onClick = onHeaderClick
        )

        if (sheetState.targetValue != SheetValue.Expanded) {
            Spacer(modifier = Modifier.height(navigationBarHeight))
        }

        RadioStations(
            state = state,
            onRadioStationClick = { radioStation ->
                scope.launch {
                    sheetState.partialExpand()
                }

                when (state.youtubePlayer) {
                    null -> {
                        onAction(
                            MusicPlayerAction.ShowYouTubePlayerError(
                                error = PlayerConstants.PlayerError.VIDEO_NOT_FOUND
                            )
                        )
                    }
                    else -> onAction(MusicPlayerAction.PlayMusic(radioStation))
                }
            }
        )
    }
}

@Composable
private fun BottomSheetHeader(
    sheetState: SheetState,
    modifier: Modifier = Modifier,
    navigationBarHeightPx: Int = getNavigationBarHeight(),
    contentPadding: PaddingValues = PaddingValues(
        start = 36.dp,
        end = 36.dp,
        top = 32.dp,
        bottom = 18.dp // 32 - 14 (from radio station item) = 18.dp
    ),
    title: AnnotatedString = AnnotatedString.fromHtml(
        htmlString = stringResource(id = R.string.music_player_radio_stations_sheet_title)
    ).toUpperCase(),
    titleFontSize: TextUnit = 16.sp,
    titleLineHeight: TextUnit = 16.sp,
    titleFontWeight: FontWeight = FontWeight.Black,
    titleColor: Color = MaterialTheme.colorScheme.onSurface,
    titleMaxLines: Int = 1,
    arrowUp: Painter = painterResource(R.drawable.ic_music_player_arrow_up),
    arrowIconColor: Color = MaterialTheme.colorScheme.onSurface,
    arrowIconWidth: Dp = 16.dp,
    arrowContentDescriptionHide: String = stringResource(R.string.music_player_radio_stations_sheet_content_description_hide),
    arrowContentDescriptionShow: String = stringResource(R.string.music_player_radio_stations_sheet_content_description_show),
    animatedRotateDegreesDuration: Int = 500,
    animatedRotateDegreesDelay: Int = 0,
    onHeaderHeightCalculated: (heightPx: Int) -> Unit,
    onClick: () -> Unit
) {
    val animatedRotateDegrees by animateFloatAsState(
        targetValue = when (sheetState.targetValue) {
            SheetValue.Expanded -> 180f
            else -> 0f
        },
        animationSpec = tween(
            durationMillis = animatedRotateDegreesDuration,
            delayMillis = animatedRotateDegreesDelay
        ),
        label = "animatedRotateDegrees"
    )

    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .fillMaxWidth()
            .clickable(
                onClick = onClick,
                interactionSource = remember { MutableInteractionSource() },
                indication = null
            )
            .onSizeChanged { size ->
                onHeaderHeightCalculated(size.height + navigationBarHeightPx)
            }
            .padding(contentPadding)
            .testTag(TestTags.BOTTOM_SHEET_HEADER)
    ) {
        Text(
            text = title,
            fontSize = titleFontSize,
            lineHeight = titleLineHeight,
            fontWeight = titleFontWeight,
            color = titleColor,
            maxLines = titleMaxLines
        )

        Icon(
            painter = arrowUp,
            contentDescription = when (sheetState.currentValue) {
                SheetValue.Expanded -> arrowContentDescriptionHide
                else -> arrowContentDescriptionShow
            },
            tint = arrowIconColor,
            modifier = Modifier
                .width(arrowIconWidth)
                .rotate(degrees = animatedRotateDegrees)
        )
    }
}

@Composable
private fun RadioStations(
    state: MusicPlayerState,
    modifier: Modifier = Modifier,
    onRadioStationClick: (radioStation: RadioStation) -> Unit
) {
    AnimatedContent(
        targetState = state.isRadioStationsLoading,
        transitionSpec = {
            fadeIn().togetherWith(exit = fadeOut())
        },
        label = "radioStationsAnimatedContent",
        modifier = modifier
            .fillMaxWidth()
            .testTag(TestTags.BOTTOM_SHEET_RADIO_STATIONS)
    ) { isLoading ->
        when {
            isLoading -> RadioStationsLoading()
            else -> RadioStationsList(
                radioStations = state.radioStations,
                onRadioStationClick = onRadioStationClick
            )
        }
    }
}