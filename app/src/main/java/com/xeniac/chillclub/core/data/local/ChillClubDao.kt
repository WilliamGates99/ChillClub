package com.xeniac.chillclub.core.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import com.xeniac.chillclub.core.data.local.entities.RadioEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ChillClubDao {

    @Upsert
    suspend fun insertRadio(radioEntity: RadioEntity): Long

    @Upsert
    suspend fun insertRadios(radioEntities: List<RadioEntity>)

    @Query("DELETE FROM radios")
    suspend fun clearRadios()

    @Delete
    suspend fun deleteRadio(radioEntity: RadioEntity)

    @Query("DELETE FROM radios WHERE id = :radioId")
    suspend fun deleteRadioById(radioId: Long)

    @Query("SELECT * FROM radios")
    suspend fun getRadios(): List<RadioEntity>

    @Query("SELECT * FROM radios")
    suspend fun observeRadios(): Flow<List<RadioEntity>>

    @Query("SELECT * FROM radios WHERE id = :id")
    fun observeRadio(id: Long): Flow<RadioEntity>
}