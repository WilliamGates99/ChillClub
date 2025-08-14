package com.xeniac.chillclub.core.presentation.common.utils.permission

import com.xeniac.chillclub.core.presentation.common.utils.UiText

interface PermissionHelper {
    fun getMessage(isPermanentlyDeclined: Boolean): UiText
}