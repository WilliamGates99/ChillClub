package com.xeniac.chillclub.feature_music_player.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.xeniac.chillclub.feature_music_player.data.db.converters.RadioEntityConverters
import com.xeniac.chillclub.feature_music_player.data.db.entities.RadioEntity

@Database(
    entities = [RadioEntity::class],
    version = 1
)
@TypeConverters(RadioEntityConverters::class)
abstract class ChillClubDatabase : RoomDatabase() {
    abstract fun dao(): ChillClubDao
}