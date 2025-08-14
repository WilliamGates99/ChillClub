package com.xeniac.chillclub.core.domain.utils

/**
 * Scales this integer value to the unit interval [0, 1] based on the provided range.
 *
 * The function ensures that even if `maxValue` is less than `minValue`,
 * a valid result within the unit interval is returned.
 *
 * @param minValue The minimum value of the input range.
 * @param maxValue The maximum value of the input range.
 * @return The scaled value between 0 and 1.
 * If `minValue` is greater than or equal to `maxValue`, returns 0.
 */
fun Int.scaleToUnitInterval(
    minValue: Int,
    maxValue: Int
): Float {
    val difference = (maxValue - minValue).coerceAtLeast(
        minimumValue = 0
    ).toFloat()

    // Avoid division by zero
    if (difference == 0f) {
        return 0f
    }

    return (this - minValue).toFloat() / difference
}

/**
 * Converts this integer value to a percentage (0 to 100) based on the provided range.
 *
 * The function utilizes `scaleToUnitInterval` to first normalize the value to the unit interval
 * and then scales it to the percentage range.
 *
 * @param minValue The minimum value of the input range.
 * @param maxValue The maximum value of the input range.
 * @return The percentage value between 0 and 100.
 * If `minValue` is greater than or equal to `maxValue`, returns 0.
 */
fun Int.convertToPercentage(
    minValue: Int,
    maxValue: Int
): Float = this.scaleToUnitInterval(
    minValue = minValue,
    maxValue = maxValue
) * 100f