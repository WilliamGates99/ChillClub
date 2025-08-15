package com.xeniac.chillclub.core.presentation.main_activity.states

import android.os.Parcelable
import com.xeniac.chillclub.core.domain.models.AppLocale
import kotlinx.parcelize.Parcelize

@Parcelize
data class MainState(
    val currentAppLocale: AppLocale = AppLocale.Default,
    val isSplashScreenLoading: Boolean = true
) : Parcelable