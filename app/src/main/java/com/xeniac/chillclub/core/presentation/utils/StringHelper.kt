package com.xeniac.chillclub.core.presentation.utils

import java.text.DecimalFormat
import java.text.DecimalFormatSymbols

fun formatNumber(input: Int): String {
    return DecimalFormat(
        /* pattern = */ "#,###",
        /* symbols = */ DecimalFormatSymbols.getInstance()
    ).format(input)
}