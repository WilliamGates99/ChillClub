package com.xeniac.chillclub.core.presentation.utils

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp

@Composable
fun Int.toDp(): Dp = with(LocalDensity.current) { this@toDp.toDp() }

@Composable
fun Dp.toPx(): Float = with(LocalDensity.current) { this@toPx.toPx() }