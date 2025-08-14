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
import com.xeniac.chillclub.core.presentation.common.ui.utils.getNavigationBarHeightDp
import com.xeniac.chillclub.core.presentation.common.ui.components.shimmerEffect
import kotlin.random.Random

@Composable
fun RadioStationsLoading(
    modifier: Modifier = Modifier,
    navigationBarHeight: Dp = getNavigationBarHeightDp()
) {
    LazyColumn(
        userScrollEnabled = false,
        horizontalAlignment = Alignment.CenterHorizontally,
        contentPadding = PaddingValues(bottom = navigationBarHeight),
        modifier = modifier
    ) {
        items(count = 6) {
            RadioStationItemLoading()
        }
    }
}

@Composable
private fun RadioStationItemLoading(
    modifier: Modifier = Modifier
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(space = 16.dp),
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier.padding(
            horizontal = 20.dp,
            vertical = 14.dp
        )
    ) {
        RadioStationCoverLoading()

        RadioStationInfoLoading(modifier = Modifier.weight(1f))
    }
}

@Composable
private fun RadioStationCoverLoading(
    modifier: Modifier = Modifier,
    coverSize: Dp = 48.dp,
    shape: Shape = RoundedCornerShape(14.dp)
) {
    Box(
        modifier = modifier
            .size(coverSize)
            .clip(shape)
            .shimmerEffect()
    )
}

@Composable
private fun RadioStationInfoLoading(
    modifier: Modifier = Modifier
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(
            space = 4.dp,
            alignment = Alignment.CenterVertically
        ),
        modifier = modifier
    ) {
        RadioStationTitleLoading()

        RadioStationTagsLoading()
    }
}

@Composable
private fun RadioStationTitleLoading(
    modifier: Modifier = Modifier,
    shape: Shape = RoundedCornerShape(4.dp),
    titleFontSize: TextUnit = 14.sp,
    titleLineHeight: TextUnit = 14.sp,
    titleFontWeight: FontWeight = FontWeight.Bold,
    titleMaxLines: Int = 1
) {
    var blankChars by rememberSaveable { mutableStateOf("") }

    LaunchedEffect(Unit) {
        for (i in 0..Random.nextInt(from = 45, until = 85)) {
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
private fun RadioStationTagsLoading(
    modifier: Modifier = Modifier
) {
    LazyRow(
        userScrollEnabled = false,
        horizontalArrangement = Arrangement.spacedBy(space = 4.dp),
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier.fillMaxWidth()
    ) {
        items(Random.nextInt(from = 1, until = 5)) {
            RadioStationTagLoading()
        }
    }
}

@Composable
private fun RadioStationTagLoading(
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