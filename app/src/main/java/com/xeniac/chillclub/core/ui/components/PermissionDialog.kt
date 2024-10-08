package com.xeniac.chillclub.core.ui.components

import android.Manifest
import android.app.Activity
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.AlertDialogDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.DialogProperties
import androidx.compose.ui.window.SecureFlagPolicy
import androidx.core.app.ActivityCompat.shouldShowRequestPermissionRationale
import com.xeniac.chillclub.R
import com.xeniac.chillclub.core.presentation.utils.PermissionHelper
import com.xeniac.chillclub.core.presentation.utils.PostNotificationsPermissionHelper
import com.xeniac.chillclub.core.presentation.utils.openAppSettings

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

@Composable
fun PermissionDialog(
    permissionHelper: PermissionHelper,
    isPermanentlyDeclined: Boolean,
    icon: Painter,
    modifier: Modifier = Modifier,
    dismissOnBackPress: Boolean = true,
    dismissOnClickOutside: Boolean = true,
    usePlatformDefaultWidth: Boolean = true,
    decorFitsSystemWindows: Boolean = true,
    securePolicy: SecureFlagPolicy = SecureFlagPolicy.Inherit,
    dialogProperties: DialogProperties = DialogProperties(
        dismissOnBackPress = dismissOnBackPress,
        dismissOnClickOutside = dismissOnClickOutside,
        usePlatformDefaultWidth = usePlatformDefaultWidth,
        decorFitsSystemWindows = decorFitsSystemWindows,
        securePolicy = securePolicy
    ),
    title: String? = null,
    confirmButtonText: String = if (isPermanentlyDeclined) stringResource(
        id = R.string.permissions_error_btn_open_settings
    ) else stringResource(
        id = R.string.permissions_error_btn_confirm
    ),
    shape: Shape = AlertDialogDefaults.shape,
    containerColor: Color = MaterialTheme.colorScheme.surface,
    iconContentColor: Color = MaterialTheme.colorScheme.secondary,
    titleContentColor: Color = MaterialTheme.colorScheme.onSurface,
    textContentColor: Color = MaterialTheme.colorScheme.onSurfaceVariant,
    dismissButtonText: String? = null,
    onConfirmClick: () -> Unit,
    onOpenAppSettingsClick: () -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        modifier = modifier,
        properties = dialogProperties,
        shape = shape,
        containerColor = containerColor,
        iconContentColor = iconContentColor,
        titleContentColor = titleContentColor,
        textContentColor = textContentColor,
        onDismissRequest = onDismiss,
        icon = {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier.size(24.dp)
            ) {
                Icon(
                    painter = icon,
                    contentDescription = null,
                    modifier = Modifier.size(24.dp)
                )
            }
        },
        title = title?.let {
            {
                Text(
                    text = title,
                    fontSize = 24.sp,
                    lineHeight = 32.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }
        },
        text = {
            Text(
                text = permissionHelper.getMessage(isPermanentlyDeclined).asString(),
                fontSize = 14.sp,
                lineHeight = 20.sp,
                fontWeight = FontWeight.Medium
            )
        },
        confirmButton = {
            TextButton(
                onClick = {
                    if (isPermanentlyDeclined) {
                        onOpenAppSettingsClick()
                    } else {
                        onConfirmClick()
                    }
                    onDismiss()
                }
            ) {
                Text(
                    text = confirmButtonText,
                    fontSize = 14.sp,
                    lineHeight = 20.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        },
        dismissButton = dismissButtonText?.let {
            {
                TextButton(onClick = onDismiss) {
                    Text(
                        text = dismissButtonText,
                        fontSize = 14.sp,
                        lineHeight = 20.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    )
}