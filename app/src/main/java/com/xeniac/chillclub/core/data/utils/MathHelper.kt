package com.xeniac.chillclub.core.data.utils

fun Int.convertToPercentage(
    minValue: Int,
    maxValue: Int
): Float = (this - minValue) / (maxValue - minValue) * 100f