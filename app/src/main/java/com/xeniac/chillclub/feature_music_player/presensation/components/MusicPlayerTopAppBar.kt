package com.xeniac.chillclub.feature_music_player.presensation.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.Text
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.xeniac.chillclub.R
import com.xeniac.chillclub.core.presentation.common.ui.theme.White
import com.xeniac.chillclub.core.presentation.common.ui.theme.WhiteAlpha64
import com.xeniac.chillclub.core.presentation.common.ui.utils.getStatusBarHeightDp
import com.xeniac.chillclub.core.presentation.common.utils.TestTags

@Composable
fun MusicPlayerTopAppBar(
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(
        start = 24.dp,
        end = 24.dp,
        top = 20.dp + getStatusBarHeightDp(),
        bottom = 20.dp
    ),
    welcome: String = stringResource(id = R.string.music_player_app_bar_title_welcome),
    welcomeStyle: TextStyle = LocalTextStyle.current.copy(
        fontSize = 12.sp,
        lineHeight = 8.sp,
        letterSpacing = 0.sp,
        fontWeight = FontWeight.Medium,
        color = WhiteAlpha64
    ),
    welcomeMaxLines: Int = 1,
    appName: String = stringResource(id = R.string.app_name).uppercase(),
    appNameStyle: TextStyle = LocalTextStyle.current.copy(
        fontSize = 14.sp,
        lineHeight = 8.sp,
        letterSpacing = 0.sp,
        fontWeight = FontWeight.Black,
        color = White
    ),
    appNameMaxLines: Int = 1,
    onSettingsClick: () -> Unit
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(space = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .fillMaxWidth()
            .padding(contentPadding)
    ) {
        MusicPlayerAppBarImage()

        Column(
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = welcome,
                style = welcomeStyle,
                maxLines = welcomeMaxLines,
                modifier = Modifier
                    .fillMaxWidth()
                    .basicMarquee()
            )

            Text(
                text = appName,
                style = appNameStyle,
                maxLines = appNameMaxLines,
                modifier = Modifier
                    .fillMaxWidth()
                    .basicMarquee()
            )
        }

        MusicPlayerAppSettingsButton(
            onClick = onSettingsClick
        )
    }
}

@Composable
private fun MusicPlayerAppBarImage(
    modifier: Modifier = Modifier,
    image: Painter = painterResource(id = R.drawable.ic_music_player_app_bar),
    size: Dp = 32.dp,
    rotationDegree: Float = -8f,
    shape: Shape = RoundedCornerShape(8.dp),
    borderStroke: BorderStroke = BorderStroke(
        width = 2.dp,
        color = White
    )
) {
    Image(
        painter = image,
        contentDescription = null,
        contentScale = ContentScale.Crop,
        modifier = modifier
            .size(size)
            .rotate(degrees = rotationDegree)
            .clip(shape)
            .border(
                border = borderStroke,
                shape = shape
            )
    )
}

@Composable
private fun MusicPlayerAppSettingsButton(
    modifier: Modifier = Modifier,
    icon: Painter = painterResource(id = R.drawable.ic_music_player_settings),
    contentDescription: String = stringResource(id = R.string.music_player_btn_settings),
    color: Color = White,
    size: Dp = 28.dp,
    contentValues: PaddingValues = PaddingValues(all = 4.dp),
    onClick: () -> Unit
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .size(size)
            .clickable(
                role = Role.Button,
                onClick = onClick,
                interactionSource = remember { MutableInteractionSource() },
                indication = ripple(bounded = false)
            )
            .padding(contentValues)
            .testTag(TestTags.BTN_SETTINGS)
    ) {
        Icon(
            painter = icon,
            contentDescription = contentDescription,
            tint = color,
            modifier = Modifier.fillMaxSize()
        )
    }
}