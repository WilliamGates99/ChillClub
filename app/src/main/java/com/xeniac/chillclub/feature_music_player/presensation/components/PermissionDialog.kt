package com.xeniac.chillclub.feature_music_player.presensation.components

import android.Manifest
import android.annotation.SuppressLint
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.xeniac.chillclub.core.presentation.utils.findActivity
import com.xeniac.chillclub.core.ui.components.NotificationPermissionDialog
import com.xeniac.chillclub.feature_music_player.presensation.states.MusicPlayerState

@Composable
fun PostNotificationPermissionHandler(
    musicPlayerState: MusicPlayerState,
    modifier: Modifier = Modifier,
    isRunningAndroid13OrNewer: Boolean = remember {
        derivedStateOf { Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU }
    }.value,
    onPermissionResult: (permission: String, isGranted: Boolean) -> Unit,
    onDismiss: (permission: String) -> Unit
) {
    @SuppressLint("InlinedApi")
    if (isRunningAndroid13OrNewer) {
        val context = LocalContext.current
        val activity = context.findActivity()

        val postNotificationPermissionResultLauncher = rememberLauncherForActivityResult(
            contract = ActivityResultContracts.RequestPermission()
        ) { isGranted ->
            onPermissionResult(
                Manifest.permission.POST_NOTIFICATIONS,
                isGranted
            )
        }

        LaunchedEffect(key1 = Unit) {
            postNotificationPermissionResultLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
        }

        NotificationPermissionDialog(
            activity = activity,
            isVisible = musicPlayerState.isPermissionDialogVisible,
            permissionQueue = musicPlayerState.permissionDialogQueue,
            onConfirmClick = {
                postNotificationPermissionResultLauncher.launch(
                    Manifest.permission.POST_NOTIFICATIONS
                )
            },
            onDismiss = onDismiss,
            modifier = modifier
        )
    }
}