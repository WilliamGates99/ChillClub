package com.xeniac.chillclub.core.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.xeniac.chillclub.core.data.local.converters.RadioStationEntityConverters
import com.xeniac.chillclub.core.data.local.entities.RadioStationEntity

@Database(
    entities = [RadioStationEntity::class],
    version = 1
)
@TypeConverters(RadioStationEntityConverters::class)
abstract class ChillClubDatabase : RoomDatabase() {
    abstract val radioStationsDao: RadioStationsDao
}