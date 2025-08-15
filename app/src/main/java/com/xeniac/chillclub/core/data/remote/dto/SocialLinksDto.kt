package com.xeniac.chillclub.core.data.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SocialLinksDto(
    @SerialName("youtube")
    val youtube: String
)