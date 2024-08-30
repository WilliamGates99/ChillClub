package com.xeniac.chillclub.core.domain.models

import android.os.Parcelable
import com.xeniac.chillclub.core.data.remote.dto.ChannelDto
import kotlinx.parcelize.Parcelize

@Parcelize
data class Channel(
    val name: String,
    val avatarUrl: String? = null,
    val socialLinks: SocialLinks? = null
) : Parcelable {
    fun toChannelDto(): ChannelDto = ChannelDto(
        name = name,
        avatarUrl = avatarUrl,
        socialLinksDto = socialLinks?.toSocialLinksDto()
    )
}