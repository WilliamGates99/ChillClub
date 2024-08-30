package com.xeniac.chillclub.core.ui.components

import androidx.compose.foundation.Indication
import androidx.compose.foundation.IndicationNodeFactory
import androidx.compose.foundation.LocalIndication
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.semantics.Role

@Composable
fun Modifier.addTestTag(tag: String?): Modifier = tag?.let { this.testTag(tag = it) } ?: this

@Composable
fun Modifier.addClickable(
    enabled: Boolean = true,
    indication: Indication? = LocalIndication.current,
    interactionSource: MutableInteractionSource? = if (indication is IndicationNodeFactory) null
    else remember { MutableInteractionSource() },
    role: Role? = null,
    onClickLabel: String? = null,
    onClick: (() -> Unit)?
): Modifier = onClick?.let {
    this.clickable(
        enabled = enabled,
        indication = indication,
        interactionSource = interactionSource,
        role = role,
        onClickLabel = onClickLabel,
        onClick = onClick
    )
} ?: this