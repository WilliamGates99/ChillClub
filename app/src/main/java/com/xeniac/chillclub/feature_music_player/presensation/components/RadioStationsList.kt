package com.xeniac.chillclub.feature_music_player.presensation.components

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
import androidx.compose.material3.LocalTextStyle
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
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.SubcomposeAsyncImage
import coil3.compose.rememberAsyncImagePainter
import com.xeniac.chillclub.R
import com.xeniac.chillclub.core.domain.models.RadioStation
import com.xeniac.chillclub.core.presentation.common.ui.components.shimmerEffect
import com.xeniac.chillclub.core.presentation.common.ui.theme.Gray10
import com.xeniac.chillclub.core.presentation.common.ui.theme.Gray20
import com.xeniac.chillclub.core.presentation.common.ui.theme.Gray40
import com.xeniac.chillclub.core.presentation.common.ui.theme.Gray70
import com.xeniac.chillclub.core.presentation.common.ui.theme.Gray90
import com.xeniac.chillclub.core.presentation.common.ui.utils.getNavigationBarHeightDp

@Composable
fun RadioStationsList(
    radioStations: List<RadioStation>,
    modifier: Modifier = Modifier,
    onRadioStationClick: (radioStation: RadioStation) -> Unit
) {
    LazyColumn(
        horizontalAlignment = Alignment.CenterHorizontally,
        contentPadding = PaddingValues(bottom = getNavigationBarHeightDp()),
        modifier = modifier.fillMaxWidth()
    ) {
        items(
            items = radioStations
        ) { radioStation ->
            RadioStationItem(
                radioStation = radioStation,
                onClick = { onRadioStationClick(radioStation) }
            )
        }
    }
}

@Composable
private fun RadioStationItem(
    radioStation: RadioStation,
    modifier: Modifier = Modifier,
    shape: Shape = RoundedCornerShape(12.dp),
    contentPadding: PaddingValues = PaddingValues(
        horizontal = 20.dp,
        vertical = 14.dp
    ),
    linkIcon: Painter = painterResource(R.drawable.ic_music_player_link),
    linkIconSize: Dp = 24.dp,
    linkIconTint: Color = if (isSystemInDarkTheme()) Gray20 else Gray70,
    onClick: () -> Unit
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(space = 16.dp),
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .fillMaxWidth()
            .clip(shape)
            .clickable { onClick() }
            .padding(contentPadding)
    ) {
        RadioStationCover(
            coverUrl = radioStation.channel.avatarUrl
        )

        RadioStationInfo(
            title = radioStation.title,
            tags = radioStation.tags,
            modifier = Modifier.weight(1f)
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
private fun RadioStationCover(
    coverUrl: String?,
    modifier: Modifier = Modifier,
    size: Dp = 48.dp,
    shape: Shape = RoundedCornerShape(14.dp),
    placeholder: Painter = rememberAsyncImagePainter(model = R.drawable.ic_music_player_cover_placeholder)
) {
    SubcomposeAsyncImage(
        model = coverUrl,
        contentDescription = stringResource(id = R.string.music_player_radio_stations_sheet_content_description_radio_cover),
        contentScale = ContentScale.Crop,
        loading = {
            Box(modifier = Modifier.shimmerEffect())
        },
        error = {
            Image(
                painter = placeholder,
                contentDescription = contentDescription,
                contentScale = contentScale
            )
        },
        modifier = modifier
            .size(size)
            .clip(shape)
    )
}

@Composable
private fun RadioStationInfo(
    title: String,
    tags: List<String>,
    modifier: Modifier = Modifier
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(
            space = 4.dp,
            alignment = Alignment.CenterVertically
        ),
        modifier = modifier
    ) {
        RadioStationTitle(title = title)

        RadioStationTags(tags = tags)
    }
}

@Composable
private fun RadioStationTitle(
    title: String,
    modifier: Modifier = Modifier,
    textStyle: TextStyle = LocalTextStyle.current.copy(
        fontSize = 14.sp,
        lineHeight = 14.sp,
        fontWeight = FontWeight.Bold,
        color = MaterialTheme.colorScheme.onSurface
    ),
    textOverflow: TextOverflow = TextOverflow.Ellipsis,
    maxLines: Int = 1
) {
    Text(
        text = title,
        style = textStyle,
        overflow = textOverflow,
        maxLines = maxLines,
        modifier = modifier
            .fillMaxWidth()
            .basicMarquee()
    )
}

@Composable
private fun RadioStationTags(
    tags: List<String>,
    modifier: Modifier = Modifier,
    shape: Shape = RoundedCornerShape(4.dp),
    background: Color = Gray90,
    textStyle: TextStyle = LocalTextStyle.current.copy(
        fontSize = 14.sp,
        lineHeight = 14.sp,
        fontWeight = FontWeight.Bold,
        color = if (isSystemInDarkTheme()) Gray10 else Gray40
    ),
    maxLines: Int = 1
) {
    LazyRow(
        userScrollEnabled = false,
        horizontalArrangement = Arrangement.spacedBy(space = 4.dp),
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier.fillMaxWidth()
    ) {
        items(
            items = tags
        ) { tag ->
            Text(
                text = tag,
                style = textStyle,
                maxLines = maxLines,
                modifier = modifier
                    .clip(shape)
                    .background(background)
                    .padding(
                        horizontal = 4.dp,
                        vertical = 2.dp
                    )
            )
        }
    }
}