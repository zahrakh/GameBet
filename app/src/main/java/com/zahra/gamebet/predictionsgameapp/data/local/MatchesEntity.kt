package com.zahra.gamebet.predictionsgameapp.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.zahra.gamebet.predictionsgameapp.domain.model.Match
import com.zahra.gamebet.predictionsgameapp.domain.model.Matches

@Entity(tableName = "matches_table")
class MatchesEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val matchList: ArrayList<MatchEntity>
) {
    fun toMatches(): Matches {
        return Matches(
            matchList.map { it.toMatch() }
        )
    }
}