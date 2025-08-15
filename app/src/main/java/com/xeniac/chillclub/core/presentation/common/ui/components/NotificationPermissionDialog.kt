package com.xeniac.chillclub.core.presentation.common.ui.components

import android.Manifest
import android.app.Activity
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.core.app.ActivityCompat.shouldShowRequestPermissionRationale
import com.xeniac.chillclub.R
import com.xeniac.chillclub.core.presentation.common.utils.openAppSettings
import com.xeniac.chillclub.core.presentation.common.utils.permission.PostNotificationsPermissionHelper

@Composable
fun NotificationPermissionDialog(
    activity: Activity,
    isVisible: Boolean,
    permissionQueue: List<String>,
    modifier: Modifier = Modifier,
    enterAnimation: EnterTransition = fadeIn() + scaleIn(),
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
