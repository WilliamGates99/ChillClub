package com.xeniac.chillclub.core.data.mappers

import com.xeniac.chillclub.core.data.remote.dto.ChannelDto
import com.xeniac.chillclub.core.domain.models.Channel

fun ChannelDto.toChannel(): Channel = Channel(
    name = name,
    avatarUrl = avatarUrl,
    socialLinks = socialLinksDto?.toSocialLinks()
)

fun Channel.toChannelDto(): ChannelDto = ChannelDto(
    name = name,
    avatarUrl = avatarUrl,
    socialLinksDto = socialLinks?.toSocialLinksDto()
)