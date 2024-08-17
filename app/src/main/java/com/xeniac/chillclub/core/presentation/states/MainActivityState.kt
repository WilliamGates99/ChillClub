package com.xeniac.chillclub.core.presentation.states

import android.os.Parcelable
import com.xeniac.chillclub.core.domain.models.AppLocale
import kotlinx.parcelize.Parcelize

@Parcelize
data class MainActivityState(
    val currentAppLocale: AppLocale = AppLocale.Default,
    val isSplashScreenLoading: Boolean = true
) : Parcelable