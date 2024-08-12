package com.xeniac.chillclub.core.ui.navigation

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable

@Parcelize
@Serializable
sealed class Screen : Parcelable {

    @Serializable
    data object MusicPlayerScreen : Screen()

    @Serializable
    data object SettingsScreen : Screen()
}