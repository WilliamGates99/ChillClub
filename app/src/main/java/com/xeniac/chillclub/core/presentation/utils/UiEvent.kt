package com.xeniac.chillclub.core.presentation.utils

import com.xeniac.chillclub.core.ui.navigation.Screen

sealed class UiEvent : Event() {
    data class ShowShortSnackbar(val message: UiText) : UiEvent()
    data class ShowLongSnackbar(val message: UiText) : UiEvent()
    data class ShowActionSnackbar(val message: UiText) : UiEvent()
    data class ShowOfflineSnackbar(val message: UiText) : UiEvent()

    data class Navigate(val screen: Screen) : UiEvent()
    data object NavigateUp : UiEvent()
}