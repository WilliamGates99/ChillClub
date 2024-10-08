package com.xeniac.chillclub.core.domain.models

import android.os.Parcelable
import com.xeniac.chillclub.core.data.remote.dto.SocialLinksDto
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable

@Serializable
@Parcelize
data class SocialLinks(
    val youtube: String
) : Parcelable {
    fun toSocialLinksDto(): SocialLinksDto = SocialLinksDto(
        youtube = youtube
    )
}