package com.xeniac.chillclub.core.data.utils

import kotlinx.datetime.Clock

typealias UnixTimeInMillis = Long
typealias UnixTimeInSeconds = Long

object DateHelper {
    fun getCurrentTimeInMillis(): UnixTimeInMillis = Clock.System.now().toEpochMilliseconds()

    fun getCurrentTimeInSeconds(): UnixTimeInSeconds = Clock.System.now().epochSeconds
}