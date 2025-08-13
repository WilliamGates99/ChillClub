package com.xeniac.chillclub.core.data.utils

import kotlin.time.Clock
import kotlin.time.ExperimentalTime

typealias UnixTimeInMs = Long
typealias UnixTimeInSeconds = Long

@OptIn(ExperimentalTime::class)
object DateHelper {
    fun getCurrentTimeInMs(): UnixTimeInMs = Clock.System.now().toEpochMilliseconds()

    fun getCurrentTimeInSeconds(): UnixTimeInSeconds = Clock.System.now().epochSeconds
}