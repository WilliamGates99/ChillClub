package com.xeniac.chillclub.feature_music_player.presensation.components

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.core.app.ActivityCompat.shouldShowRequestPermissionRationale
import com.xeniac.chillclub.R
import com.xeniac.chillclub.core.presentation.utils.findActivity
import com.xeniac.chillclub.core.presentation.utils.openAppSettings
import com.xeniac.chillclub.core.ui.components.PermissionDialog
import com.xeniac.chillclub.feature_music_player.presensation.states.MusicPlayerState
import com.xeniac.chillclub.feature_music_player.presensation.utils.PostNotificationsPermissionHelper

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

@Composable
fun NotificationPermissionDialog(
    activity: Activity,
    isVisible: Boolean,
    permissionQueue: List<String>,
    modifier: Modifier = Modifier,
    enterAnimation: EnterTransition = scaleIn() + fadeIn(),
    exitAnimation: ExitTransition = scaleOut() + fadeOut(),
    onConfirmClick: () -> Unit,
    onDismiss: (permission: String) -> Unit
) {
    AnimatedVisibility(
        visible = isVisible,
        enter = enterAnimation,
        exit = exitAnimation,
        modifier = modifier
    ) {
        permissionQueue.reversed().forEach { permission ->
            PermissionDialog(
                icon = painterResource(id = R.drawable.ic_dialog_post_notification),
                permissionHelper = when (permission) {
                    Manifest.permission.POST_NOTIFICATIONS -> PostNotificationsPermissionHelper()
                    else -> return@forEach
                },
                isPermanentlyDeclined = !shouldShowRequestPermissionRationale(
                    /* activity = */ activity,
                    /* permission = */ permission
                ),
                onConfirmClick = onConfirmClick,
                onOpenAppSettingsClick = activity::openAppSettings,
                onDismiss = { onDismiss(permission) }
            )
        }
    }
}