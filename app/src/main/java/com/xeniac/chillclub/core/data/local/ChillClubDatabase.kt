package com.xeniac.chillclub.core.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.xeniac.chillclub.core.data.local.converters.RadioEntityConverters
import com.xeniac.chillclub.core.data.local.entities.RadioEntity

@Database(
    entities = [RadioEntity::class],
    version = 1
)
@TypeConverters(RadioEntityConverters::class)
abstract class ChillClubDatabase : RoomDatabase() {
    abstract val dao: ChillClubDao
}