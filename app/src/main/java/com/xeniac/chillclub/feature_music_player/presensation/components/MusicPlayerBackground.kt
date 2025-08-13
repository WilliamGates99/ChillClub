package com.xeniac.chillclub.feature_music_player.presensation.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import coil3.compose.SubcomposeAsyncImage
import com.xeniac.chillclub.R
import com.xeniac.chillclub.core.ui.components.shimmerEffect
import kotlin.random.Random

@Composable
fun MusicPlayerBackground(
    modifier: Modifier = Modifier,
    backgroundGifs: List<Int> = listOf(
        R.raw.gif_1,
        R.raw.gif_2,
        R.raw.gif_3,
        R.raw.gif_4,
        R.raw.gif_5,
        R.raw.gif_6,
        R.raw.gif_7,
        R.raw.gif_8,
        R.raw.gif_9,
        R.raw.gif_10,
        R.raw.gif_11
    ),
    contentScale: ContentScale = ContentScale.Crop
) {
    val backgroundIndex by rememberSaveable {
        mutableIntStateOf(Random.nextInt(from = 0, until = backgroundGifs.size))
    }

    SubcomposeAsyncImage(
        model = remember { backgroundGifs[backgroundIndex] },
        contentDescription = null,
        contentScale = contentScale,
        loading = {
            Box(modifier = Modifier.shimmerEffect())
        },
        modifier = modifier.fillMaxSize()
    )
}