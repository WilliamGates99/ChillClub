package com.xeniac.chillclub.core.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "radio_stations_version")
data class RadioStationsVersionEntity(
    val version: Int,
    @PrimaryKey(autoGenerate = true)
    val id: Long? = null
)