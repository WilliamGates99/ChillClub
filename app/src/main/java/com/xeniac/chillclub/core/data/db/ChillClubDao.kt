package com.xeniac.chillclub.core.data.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import com.xeniac.chillclub.core.data.db.entities.RadioEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ChillClubDao {

    @Upsert
    suspend fun insertRadioEntity(radioEntity: RadioEntity): Long

    @Upsert
    suspend fun insertRadioEntities(radioEntities: List<RadioEntity>)

    @Query("DELETE FROM radios")
    suspend fun clearRadioEntities()

    @Delete
    suspend fun deleteRadioEntity(radioEntity: RadioEntity)

    @Query("DELETE FROM radios WHERE id = :radioId")
    suspend fun deleteRadioEntityById(radioId: Long)

    @Query("SELECT * FROM radios")
    fun getRadioEntities(): List<RadioEntity>

    @Query("SELECT * FROM radios WHERE id = :id")
    fun observeRadio(id: Long): Flow<RadioEntity>
}