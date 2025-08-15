package com.xeniac.chillclub.core.domain.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable

@Serializable
@Parcelize
data class SocialLinks(
    val youtube: String
) : Parcelable