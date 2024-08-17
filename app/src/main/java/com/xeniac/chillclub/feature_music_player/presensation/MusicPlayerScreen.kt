package com.xeniac.chillclub.feature_music_player.presensation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import com.xeniac.chillclub.core.presentation.utils.TestTags.BTN_SETTINGS

@Composable
fun MusicPlayerScreen(
    onNavigateToSettingsScreen: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier.fillMaxSize()
    ) {
        Text(text = "MusicPlayerScreen")

        Button(
            onClick = onNavigateToSettingsScreen,
            modifier = Modifier.testTag(BTN_SETTINGS)
        ) {
            Text(text = "Settings")
        }
    }
}