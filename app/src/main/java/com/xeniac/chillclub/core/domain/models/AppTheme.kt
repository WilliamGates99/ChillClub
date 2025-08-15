package com.xeniac.chillclub.core.domain.models

import android.os.Parcelable
import androidx.appcompat.app.AppCompatDelegate
import kotlinx.parcelize.Parcelize

@Parcelize
sealed class AppTheme(
    val index: Int,
    val setAppTheme: () -> Unit
) : Parcelable {
    data object Light : AppTheme(
        index = 1,
        setAppTheme = { AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO) }
    )

    data object Dark : AppTheme(
        index = 2,
        setAppTheme = { AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES) }
    )
}