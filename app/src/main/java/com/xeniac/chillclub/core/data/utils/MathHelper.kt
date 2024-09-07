package com.xeniac.chillclub.core.data.utils

fun Int.convertToPercentage(
    minValue: Int,
    maxValue: Int
): Float = (this - minValue).toFloat() / (maxValue - minValue).toFloat() * 100f