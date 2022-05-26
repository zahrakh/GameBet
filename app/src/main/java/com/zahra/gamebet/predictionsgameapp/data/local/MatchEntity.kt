package com.zahra.gamebet.predictionsgameapp.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.zahra.gamebet.predictionsgameapp.domain.model.Match

@Entity(tableName = "match_table")
class MatchEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int?=null,
    val team1: String,
    val team2: String,
    val point1: Int?,
    val point2: Int?,
    ) {
    fun toMatch(): Match {
        return Match(
            team1 = team1,
            team2 = team2,
            team1Point =point1,
            team2Point =point2
        )
    }
}