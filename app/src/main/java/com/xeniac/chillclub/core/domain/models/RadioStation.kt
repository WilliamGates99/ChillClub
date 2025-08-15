package com.xeniac.chillclub.core.domain.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class RadioStation(
    val youtubeVideoId: String,
    val title: String,
    val channel: Channel,
    val category: String,
    val tags: List<String>,
    val id: Long? = null
) : Parcelable