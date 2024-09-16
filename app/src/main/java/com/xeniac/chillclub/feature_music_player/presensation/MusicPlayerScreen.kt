package com.xeniac.chillclub.feature_music_player.presensation

import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SheetValue
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.material3.rememberStandardBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.xeniac.chillclub.core.presentation.utils.ObserverAsEvent
import com.xeniac.chillclub.core.presentation.utils.UiEvent
import com.xeniac.chillclub.core.presentation.utils.getStatusBarHeightDp
import com.xeniac.chillclub.core.presentation.utils.toDp
import com.xeniac.chillclub.core.ui.components.SwipeableSnackbar
import com.xeniac.chillclub.feature_music_player.presensation.components.MusicPlayer
import com.xeniac.chillclub.feature_music_player.presensation.components.MusicPlayerBackground
import com.xeniac.chillclub.feature_music_player.presensation.components.MusicPlayerTopAppBar
import com.xeniac.chillclub.feature_music_player.presensation.components.PostNotificationPermissionHandler
import com.xeniac.chillclub.feature_music_player.presensation.components.RadioStationsBottomSheet
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
    var bottomSheetPeekHeightPx by remember { mutableIntStateOf(0) }

    val bottomSheetScaffoldState = rememberBottomSheetScaffoldState(
        snackbarHostState = snackbarHostState,
        bottomSheetState = rememberStandardBottomSheetState(
            skipHiddenState = true,
            initialValue = SheetValue.PartiallyExpanded,
            confirmValueChange = { sheetValue ->
                val shouldActionBeConfirmed = sheetValue != SheetValue.Hidden
                shouldActionBeConfirmed
            }
        )
    )

    ObserverAsEvent(flow = viewModel.getRadioStationsEventChannel) { event ->
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

    BottomSheetScaffold(
        scaffoldState = bottomSheetScaffoldState,
        snackbarHost = { SwipeableSnackbar(hostState = snackbarHostState) },
        sheetContent = {
            RadioStationsBottomSheet(
                musicPlayerState = musicPlayerState,
                sheetState = bottomSheetScaffoldState.bottomSheetState,
                onHeaderHeightCalculated = { heightPx ->
                    bottomSheetPeekHeightPx = heightPx
                },
                onHeaderClick = {
                    scope.launch {
                        when (bottomSheetScaffoldState.bottomSheetState.currentValue) {
                            SheetValue.PartiallyExpanded -> {
                                bottomSheetScaffoldState.bottomSheetState.expand()
                            }
                            SheetValue.Expanded -> {
                                bottomSheetScaffoldState.bottomSheetState.partialExpand()
                            }
                            else -> Unit
                        }
                    }
                },
                onRadioStationClick = { radioStation ->
                    scope.launch {
                        bottomSheetScaffoldState.bottomSheetState.partialExpand()
                    }
                    // TODO: IMPLEMENT
                },
                modifier = Modifier.fillMaxWidth()
            )
        },
        sheetPeekHeight = bottomSheetPeekHeightPx.toDp(),
        sheetShape = RoundedCornerShape(
            topStart = 36.dp,
            topEnd = 36.dp
        ),
        sheetContainerColor = MaterialTheme.colorScheme.surface,
        sheetDragHandle = null,
        containerColor = Color.Transparent,
        modifier = Modifier
            .fillMaxSize()
            .nestedScroll(scrollBehavior.nestedScrollConnection)
    ) { innerPadding ->
        MusicPlayerBackground()

        Column(
            modifier = Modifier
                .fillMaxSize()
                .pointerInput(key1 = Unit) {
                    detectTapGestures { tapOffset ->
                        musicPlayerState.volumeSliderBounds?.let { sliderBounds ->
                            val isWithinSliderWidth = tapOffset.x >= sliderBounds.left
                                    && tapOffset.x <= sliderBounds.right
                            val isWithinSliderHeight = tapOffset.y >= sliderBounds.top
                                    && tapOffset.y <= sliderBounds.bottom

                            val isWithinSliderBounds = isWithinSliderWidth && isWithinSliderHeight

                            val shouldHideVolumeSlider = !isWithinSliderBounds
                                    && musicPlayerState.isVolumeSliderVisible
                            if (shouldHideVolumeSlider) {
                                viewModel.onAction(MusicPlayerAction.HideVolumeSlider)
                            }
                        }
                    }
                }
        ) {
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

            MusicPlayer(
                musicPlayerState = musicPlayerState,
                onAction = viewModel::onAction,
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .padding(
                        top = 8.dp,
                        bottom = innerPadding.calculateBottomPadding() + 8.dp
                    )
            )
        }
    }
}