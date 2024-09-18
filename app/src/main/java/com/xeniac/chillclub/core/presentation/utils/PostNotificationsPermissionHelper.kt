package com.xeniac.chillclub.core.presentation.utils

import com.xeniac.chillclub.R

class PostNotificationsPermissionHelper : PermissionHelper {
    override fun getMessage(isPermanentlyDeclined: Boolean): UiText {
        return if (isPermanentlyDeclined) {
            UiText.StringResource(R.string.permissions_error_notification_declined_permanently)
        } else {
            UiText.StringResource(R.string.permissions_error_notification_declined)
        }
    }
}