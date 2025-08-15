package com.xeniac.chillclub.core.presentation.common.ui.components

import androidx.annotation.IntRange
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderColors
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.SliderState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.layout
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.Dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VerticalSlider(
    value: Float,
    width: Dp,
    height: Dp,
    modifier: Modifier = Modifier,
    paddingValues: PaddingValues = PaddingValues(),
    isEnabled: Boolean = true,
    colors: SliderColors = SliderDefaults.colors(),
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    valueRange: ClosedFloatingPointRange<Float> = 0f..1f,
    @IntRange(from = 0)
    steps: Int = 0,
    thumb: @Composable (SliderState) -> Unit = {
        SliderDefaults.Thumb(
            enabled = isEnabled,
            colors = colors,
            interactionSource = interactionSource
        )
    },
    track: @Composable (SliderState) -> Unit = { sliderState ->
        SliderDefaults.Track(
            enabled = isEnabled,
            colors = colors,
            sliderState = sliderState
        )
    },
    onValueChangeFinished: (() -> Unit)? = null,
    onValueChange: (newValue: Float) -> Unit
) {
    Slider(
        enabled = isEnabled,
        value = value,
        onValueChange = onValueChange,
        onValueChangeFinished = onValueChangeFinished,
        valueRange = valueRange,
        steps = steps,
        thumb = thumb,
        track = track,
        modifier = modifier
            .graphicsLayer {
                rotationZ = -90f
                transformOrigin = TransformOrigin(
                    pivotFractionX = 0f,
                    pivotFractionY = 0f
                )
            }
            .layout { measurable, constraints ->
                val placeable = measurable.measure(
                    Constraints(
                        minWidth = constraints.minHeight,
                        maxWidth = constraints.maxHeight,
                        minHeight = constraints.minWidth,
                        maxHeight = constraints.maxHeight
                    )
                )

                layout(
                    width = placeable.height,
                    height = placeable.width
                ) {
                    placeable.place(
                        x = -placeable.width,
                        y = 0
                    )
                }
            }
            .width(height)
            .height(width)
            .padding(paddingValues)
    )
}