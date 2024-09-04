package com.xeniac.chillclub.feature_music_player.presensation.components

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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.xeniac.chillclub.core.ui.components.shimmerEffect
import kotlin.random.Random

@Composable
fun RadioStationsLoading(
    modifier: Modifier = Modifier
) {
    LazyColumn(
        userScrollEnabled = false,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(space = 12.dp),
        contentPadding = PaddingValues(),
        modifier = modifier
    ) {
        items(count = 50) {
            RadioStationItemLoading()
        }
    }
}

@Composable
fun RadioStationItemLoading(
    modifier: Modifier = Modifier,
    coverSize: Dp = 44.dp,
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(space = 14.dp),
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .padding(
                horizontal = 24.dp,
                vertical = 12.dp
            )
    ) {
        RadioStationCoverLoading(
            modifier = Modifier.size(coverSize)
        )

        RadioStationInfoLoading(modifier = Modifier.weight(1f))
    }
}

@Composable
fun RadioStationCoverLoading(
    modifier: Modifier = Modifier,
    shape: Shape = RoundedCornerShape(14.dp)
) {
    Box(
        modifier = modifier
            .clip(shape)
            .shimmerEffect()
    )
}

@Composable
fun RadioStationInfoLoading(
    modifier: Modifier = Modifier
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(space = 4.dp),
        modifier = modifier
    ) {
        RadioStationTitleLoading()

        LazyRow(
            userScrollEnabled = false,
            horizontalArrangement = Arrangement.spacedBy(space = 4.dp),
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            items(Random.nextInt(from = 1, until = 5)) {
                RadioStationTagLoading()
            }
        }
    }
}

@Composable
fun RadioStationTitleLoading(
    modifier: Modifier = Modifier,
    shape: Shape = RoundedCornerShape(4.dp),
    titleFontSize: TextUnit = 14.sp,
    titleLineHeight: TextUnit = 14.sp,
    titleFontWeight: FontWeight = FontWeight.Bold,
    titleMaxLines: Int = 1
) {
    var blankChars by rememberSaveable { mutableStateOf("") }

    LaunchedEffect(Unit) {
        for (i in 0..Random.nextInt(from = 60, until = 100)) {
            blankChars += " "
        }
    }

    Text(
        text = blankChars,
        fontSize = titleFontSize,
        lineHeight = titleLineHeight,
        fontWeight = titleFontWeight,
        maxLines = titleMaxLines,
        modifier = modifier
            .clip(shape)
            .shimmerEffect()
    )
}

@Composable
fun RadioStationTagLoading(
    modifier: Modifier = Modifier,
    shape: Shape = RoundedCornerShape(4.dp),
    tagFontSize: TextUnit = 14.sp,
    tagLineHeight: TextUnit = 14.sp,
    tagFontWeight: FontWeight = FontWeight.Bold,
    tagMaxLines: Int = 1
) {
    var blankChars by rememberSaveable { mutableStateOf("") }

    LaunchedEffect(Unit) {
        for (i in 0..Random.nextInt(from = 8, until = 16)) {
            blankChars += " "
        }
    }

    Text(
        text = blankChars,
        fontSize = tagFontSize,
        lineHeight = tagLineHeight,
        fontWeight = tagFontWeight,
        maxLines = tagMaxLines,
        modifier = modifier
            .clip(shape)
            .shimmerEffect()
            .padding(
                horizontal = 4.dp,
                vertical = 2.dp
            )
    )
}