package com.xeniac.chillclub.core.data.mappers

import com.xeniac.chillclub.core.data.remote.dto.SocialLinksDto
import com.xeniac.chillclub.core.domain.models.SocialLinks

fun SocialLinksDto.toSocialLinks(): SocialLinks = SocialLinks(
    youtube = youtube
)

fun SocialLinks.toSocialLinksDto(): SocialLinksDto = SocialLinksDto(
    youtube = youtube
)