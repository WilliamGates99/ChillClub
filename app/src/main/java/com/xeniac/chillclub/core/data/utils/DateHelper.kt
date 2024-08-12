package com.xeniac.chillclub.core.data.utils

import kotlinx.datetime.Clock

object DateHelper {
    fun getCurrentTimeInMillis(): Long = Clock.System.now().toEpochMilliseconds()
}