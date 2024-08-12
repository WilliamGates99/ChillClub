package com.xeniac.chillclub.core.presentation.utils

interface PermissionHelper {
    fun getMessage(isPermanentlyDeclined: Boolean): UiText
}