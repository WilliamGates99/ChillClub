package com.xeniac.chillclub.feature_music_player.presensation.utils

import com.xeniac.chillclub.R
import com.xeniac.chillclub.core.presentation.utils.PermissionHelper
import com.xeniac.chillclub.core.presentation.utils.UiText

class PostNotificationsPermissionHelper : PermissionHelper {
    override fun getMessage(isPermanentlyDeclined: Boolean): UiText {
        return if (isPermanentlyDeclined) {
            UiText.StringResource(R.string.permissions_error_notification_declined_permanently)
        } else {
            UiText.StringResource(R.string.permissions_error_notification_declined)
        }
    }
}