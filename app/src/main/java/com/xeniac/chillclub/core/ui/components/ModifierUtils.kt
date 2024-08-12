package com.xeniac.chillclub.core.ui.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag

@Composable
fun Modifier.addTestTag(tag: String?): Modifier = tag?.let { this.testTag(tag = it) } ?: this