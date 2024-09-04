package com.xeniac.chillclub.feature_music_player.presensation.components

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.SubcomposeAsyncImage
import coil.compose.rememberAsyncImagePainter
import com.xeniac.chillclub.R
import com.xeniac.chillclub.core.domain.models.RadioStation
import com.xeniac.chillclub.core.ui.components.shimmerEffect
import com.xeniac.chillclub.core.ui.theme.Gray10
import com.xeniac.chillclub.core.ui.theme.Gray20
import com.xeniac.chillclub.core.ui.theme.Gray60
import com.xeniac.chillclub.core.ui.theme.Gray70
import com.xeniac.chillclub.core.ui.theme.Gray90
import com.xeniac.chillclub.feature_music_player.presensation.states.MusicPlayerState

@Composable
fun RadioStations(
    musicPlayerState: MusicPlayerState,
    modifier: Modifier = Modifier,
    onRadioStationClick: (radioStation: RadioStation) -> Unit
) {
    AnimatedContent(
        targetState = musicPlayerState.isRadioStationsLoading,
        label = "radioStationsAnimatedContent",
        transitionSpec = {
            fadeIn().togetherWith(exit = fadeOut())
        },
        modifier = modifier
    ) { isLoading ->
        if (isLoading) {
            RadioStationsLoading(modifier = modifier)
        } else {
            RadioStationsList(
                radioStations = musicPlayerState.radioStations,
                onRadioStationClick = onRadioStationClick,
                modifier = modifier
            )
        }
    }
}

@Composable
fun RadioStationsList(
    radioStations: List<RadioStation>,
    modifier: Modifier = Modifier,
    onRadioStationClick: (radioStation: RadioStation) -> Unit
) {
    LazyColumn(
        userScrollEnabled = false,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(space = 12.dp),
        contentPadding = PaddingValues(),
        modifier = modifier
    ) {
        items(radioStations) { radioStation ->
            RadioStationItem(
                radioStation = radioStation,
                onClick = { onRadioStationClick(radioStation) },
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

@Composable
fun RadioStationItem(
    radioStation: RadioStation,
    modifier: Modifier = Modifier,
    coverSize: Dp = 44.dp,
    linkIcon: Painter = painterResource(R.drawable.ic_music_player_link),
    linkIconSize: Dp = 24.dp,
    linkIconTint: Color = if (isSystemInDarkTheme()) Gray70 else Gray20,
    onClick: () -> Unit
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(space = 14.dp),
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .clickable { onClick() }
            .padding(
                horizontal = 24.dp,
                vertical = 12.dp
            )
    ) {
        RadioStationCover(
            coverUrl = radioStation.channel.avatarUrl,
            modifier = Modifier.size(coverSize)
        )

        RadioStationInfo(
            title = radioStation.title,
            tags = radioStation.tags,
            modifier = Modifier.weight(1f),
        )

        Icon(
            painter = linkIcon,
            contentDescription = null,
            tint = linkIconTint,
            modifier = Modifier.size(linkIconSize)
        )
    }
}

@Composable
fun RadioStationCover(
    coverUrl: String?,
    modifier: Modifier = Modifier,
    shape: Shape = RoundedCornerShape(14.dp),
    contentScale: ContentScale = ContentScale.Crop,
    placeholder: Painter = rememberAsyncImagePainter(model = R.drawable.ic_music_player_cover_placeholder),
    contentDescription: String = stringResource(id = R.string.music_player_radio_stations_sheet_content_description_radio_cover)
) {
    SubcomposeAsyncImage(
        model = coverUrl,
        contentDescription = contentDescription,
        contentScale = contentScale,
        loading = {
            Box(
                modifier = modifier
                    .clip(shape)
                    .shimmerEffect()
            )
        },
        error = {
            Image(
                painter = placeholder,
                contentDescription = contentDescription,
                contentScale = contentScale,
                modifier = modifier.clip(shape)
            )
        },
        modifier = modifier.clip(shape)
    )
}

@Composable
fun RadioStationInfo(
    title: String,
    tags: List<String>,
    modifier: Modifier = Modifier,
    titleFontSize: TextUnit = 14.sp,
    titleLineHeight: TextUnit = 14.sp,
    titleFontWeight: FontWeight = FontWeight.Bold,
    titleColor: Color = MaterialTheme.colorScheme.onSurface,
    titleMaxLines: Int = 1
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(space = 4.dp),
        modifier = modifier
    ) {
        Text(
            text = title,
            fontSize = titleFontSize,
            lineHeight = titleLineHeight,
            fontWeight = titleFontWeight,
            color = titleColor,
            maxLines = titleMaxLines,
            modifier = Modifier
                .fillMaxWidth()
                .basicMarquee()
        )

        LazyRow(
            userScrollEnabled = false,
            horizontalArrangement = Arrangement.spacedBy(space = 4.dp),
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            items(tags) { tag ->
                RadioStationTag(tag = tag)
            }
        }
    }
}

// TODO: EDIT TAG COLORS
@Composable
fun RadioStationTag(
    tag: String,
    modifier: Modifier = Modifier,
    shape: Shape = RoundedCornerShape(4.dp),
    background: Color = if (isSystemInDarkTheme()) Gray90 else Gray20,
    tagFontSize: TextUnit = 14.sp,
    tagLineHeight: TextUnit = 14.sp,
    tagFontWeight: FontWeight = FontWeight.Bold,
    tagColor: Color = if (isSystemInDarkTheme()) Gray10 else Gray60,
    tagMaxLines: Int = 1
) {
    Text(
        text = tag,
        fontSize = tagFontSize,
        lineHeight = tagLineHeight,
        fontWeight = tagFontWeight,
        color = tagColor,
        maxLines = tagMaxLines,
        modifier = modifier
            .clip(shape)
            .background(background)
            .padding(
                horizontal = 4.dp,
                vertical = 2.dp
            )
    )
}