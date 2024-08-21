package com.xeniac.chillclub.core.data.local.mapper

import com.xeniac.chillclub.core.data.local.dto.RateAppOptionDto
import com.xeniac.chillclub.core.domain.models.RateAppOption

fun RateAppOption.toRateAppOptionDto(): RateAppOptionDto = RateAppOptionDto.valueOf(this.toString())

fun RateAppOptionDto.toRateAppOption(): RateAppOption = RateAppOption.valueOf(this.toString())