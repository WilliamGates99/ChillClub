package com.xeniac.chillclub.feature_music_player.data.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import com.xeniac.chillclub.feature_music_player.data.db.entities.RadioEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ChillClubDao {

    @Query("SELECT COUNT(id) FROM radios")
    suspend fun getRadiosCount(): Int

    @Upsert
    suspend fun insertRadios(vararg radioEntity: RadioEntity)

    @Query("DELETE FROM radios")
    suspend fun clearRadios()

    @Delete
    suspend fun deleteRadio(radioEntity: RadioEntity)

    @Query("DELETE FROM radios WHERE id = :radioId")
    suspend fun deleteRadio(radioId: Long)

    @Query("SELECT * FROM radios")
    fun getRadios(): List<RadioEntity>

    @Query("SELECT * FROM radios WHERE id = :id")
    fun observeRadio(id: Long): Flow<RadioEntity>
}