package com.xeniac.chillclub.core.ui.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.SwipeToDismissBoxState
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.material3.rememberSwipeToDismissBoxState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.unit.LayoutDirection

@Composable
fun SwipeableSnackbar(
    hostState: SnackbarHostState,
    modifier: Modifier = Modifier,
    dismissSnackbarState: SwipeToDismissBoxState = rememberSwipeToDismissBoxState(
        confirmValueChange = { value ->
            if (value != SwipeToDismissBoxValue.Settled) {
                hostState.currentSnackbarData?.dismiss()
                true
            } else false
        }
    )
) {
    val layoutDirection = LocalLayoutDirection.current

    // Set the layout direction to LTR to solve the opposite swipe direction in RTL layouts
    CompositionLocalProvider(value = LocalLayoutDirection provides LayoutDirection.Ltr) {
        LaunchedEffect(dismissSnackbarState.currentValue) {
            if (dismissSnackbarState.currentValue != SwipeToDismissBoxValue.Settled) {
                dismissSnackbarState.reset()
            }
        }

        SwipeToDismissBox(
            state = dismissSnackbarState,
            backgroundContent = {},
            modifier = modifier.fillMaxWidth()
        ) {
            CompositionLocalProvider(value = LocalLayoutDirection provides layoutDirection) {
                SnackbarHost(hostState = hostState)
            }
        }
    }
}