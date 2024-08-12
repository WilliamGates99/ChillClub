package com.xeniac.chillclub.core.domain.states

import android.os.Parcelable
import com.xeniac.chillclub.core.presentation.utils.UiText
import kotlinx.parcelize.Parcelize

@Parcelize
data class CustomTextFieldState(
    val text: String = "",
    val isValid: Boolean = false,
    val errorText: UiText? = null
) : Parcelable