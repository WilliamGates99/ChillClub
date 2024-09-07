package com.xeniac.chillclub.feature_music_player.presensation.components

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.xeniac.chillclub.R
import com.xeniac.chillclub.core.ui.components.CustomIconButton
import com.xeniac.chillclub.feature_music_player.presensation.states.MusicPlayerState

@Composable
fun MusicPlayerMediaControls(
    musicPlayerState: MusicPlayerState,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current

    Row(
        horizontalArrangement = Arrangement.spacedBy(
            space = 8.dp,
            alignment = Alignment.Start
        ),
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
    ) {
        CustomIconButton(
            icon = painterResource(R.drawable.ic_music_player_play),
            contentDescription = null,
            onClick = {
                Toast.makeText(context, "clicked", Toast.LENGTH_SHORT).show()
            }
        )

        CustomIconButton(
            icon = painterResource(R.drawable.ic_music_player_volume_full),
            contentDescription = null,
            onClick = {
                Toast.makeText(context, "clicked", Toast.LENGTH_SHORT).show()
            }
        )
    }
}