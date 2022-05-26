package com.zahra.gamebet.predictionsgameapp.data.local

import androidx.room.*
import com.zahra.gamebet.predictionsgameapp.domain.model.Point
import kotlinx.coroutines.flow.Flow

@Dao
interface MatchesDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMatchesEntity(matches: List<MatchEntity>)

    @Query("SELECT * FROM match_table")
    suspend fun getMatchEntities(): List<MatchEntity>

    @Query("DELETE FROM match_table")
    suspend fun delete()

    @Query("UPDATE match_table SET point1=:point1  ,point2=:point2 WHERE team1 = :team1name AND team2 = :team2name")
    suspend fun update(team1name: String, team2name: String, point1: Int?, point2: Int?)


    @Query("UPDATE match_table SET point1=null  ,point2=null")
    fun reset()
}