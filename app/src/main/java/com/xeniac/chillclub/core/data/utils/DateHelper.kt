package com.xeniac.chillclub.core.data.utils

import kotlinx.datetime.Clock

typealias UnixTimeInMs = Long
typealias UnixTimeInSeconds = Long

object DateHelper {
    fun getCurrentTimeInMs(): UnixTimeInMs = Clock.System.now().toEpochMilliseconds()

    fun getCurrentTimeInSeconds(): UnixTimeInSeconds = Clock.System.now().epochSeconds
}