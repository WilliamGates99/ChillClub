package com.xeniac.chillclub.core.data.local.dto

enum class RateAppOptionDto(val value: String) {
    NOT_SHOWN_YET(value = "notShownYet"),
    NEVER(value = "never"),
    REMIND_LATER(value = "remindLater"),
    RATE_NOW(value = "rateNow")
}