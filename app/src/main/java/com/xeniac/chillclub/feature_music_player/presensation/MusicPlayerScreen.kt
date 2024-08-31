package com.xeniac.chillclub.feature_music_player.presensation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.xeniac.chillclub.core.presentation.utils.ObserverAsEvent
import com.xeniac.chillclub.core.presentation.utils.UiEvent
import com.xeniac.chillclub.core.presentation.utils.getStatusBarHeightDp
import com.xeniac.chillclub.core.ui.components.SwipeableSnackbar
import com.xeniac.chillclub.core.ui.theme.White
import com.xeniac.chillclub.feature_music_player.presensation.components.MusicPlayerBackground
import com.xeniac.chillclub.feature_music_player.presensation.components.MusicPlayerTopAppBar
import com.xeniac.chillclub.feature_music_player.presensation.components.PostNotificationPermissionHandler
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MusicPlayerScreen(
    onNavigateToSettingsScreen: () -> Unit,
    viewModel: MusicPlayerViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()

    val musicPlayerState by viewModel.musicPlayerState.collectAsStateWithLifecycle()

    ObserverAsEvent(flow = viewModel.getRadiosEventChannel) { event ->
        when (event) {
            is UiEvent.ShowLongSnackbar -> {
                scope.launch {
                    snackbarHostState.currentSnackbarData?.dismiss()

                    snackbarHostState.showSnackbar(
                        message = event.message.asString(context),
                        duration = SnackbarDuration.Long
                    )
                }
            }
        }
    }

    ObserverAsEvent(flow = viewModel.adjustMusicVolumeEventChannel) { event ->
        when (event) {
            is UiEvent.ShowLongSnackbar -> {
                scope.launch {
                    snackbarHostState.currentSnackbarData?.dismiss()

                    snackbarHostState.showSnackbar(
                        message = event.message.asString(context),
                        duration = SnackbarDuration.Long
                    )
                }
            }
            else -> Unit
        }
    }

    PostNotificationPermissionHandler(
        musicPlayerState = musicPlayerState,
        onPermissionResult = { permission, isGranted ->
            viewModel.onAction(
                MusicPlayerAction.OnPermissionResult(
                    permission = permission,
                    isGranted = isGranted
                )
            )
        },
        onDismiss = { permission ->
            viewModel.onAction(MusicPlayerAction.DismissPermissionDialog(permission))
        }
    )

    Scaffold(
        snackbarHost = { SwipeableSnackbar(hostState = snackbarHostState) },
        topBar = {
            MusicPlayerTopAppBar(
                onSettingsClick = onNavigateToSettingsScreen,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        start = 24.dp,
                        end = 24.dp,
                        top = 20.dp + getStatusBarHeightDp(),
                        bottom = 20.dp
                    )
            )
        },
        containerColor = Color.Transparent,
        modifier = Modifier
            .fillMaxSize()
            .nestedScroll(scrollBehavior.nestedScrollConnection)
    ) { innerPadding ->
        Box(modifier = Modifier.fillMaxSize()) {
            MusicPlayerBackground()

            Column(
                horizontalAlignment = Alignment.CenterHorizontally, // TODO: TEMP
                verticalArrangement = Arrangement.spacedBy(
                    16.dp,
                    Alignment.CenterVertically
                ), // TODO: TEMP
                modifier = Modifier
                    .fillMaxSize()
                    .padding(
                        top = innerPadding.calculateTopPadding() + 4.dp,
                        bottom = innerPadding.calculateBottomPadding()
                    )
            ) {
                Text(
                    text = "This is a test",
                    fontSize = 16.sp,
                    color = White
                )

                Button(onClick = {
                    viewModel.onAction(MusicPlayerAction.IncreaseMusicVolume)
                }) {
                    Text(text = "Increase Volume")
                }

                Button(onClick = {
                    viewModel.onAction(MusicPlayerAction.DecreaseMusicVolume)
                }) {
                    Text(text = "Decrease Volume")
                }
            }
        }
    }
}