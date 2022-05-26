package com.zahra.gamebet.predictionsgameapp.data.local

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface MatchDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMatchEntity(matchEntity: MatchEntity)

    @Query("SELECT * FROM match_table")
    fun getMatchEntities(): List<MatchEntity>

    @Delete
    suspend fun deleteMatchEntity(matchEntity: MatchEntity)

    @Query("SELECT * FROM match_table")
    suspend fun getMatchEntity(): MatchEntity?

}