package com.zahra.gamebet.predictionsgameapp.domain.model

import com.zahra.gamebet.predictionsgameapp.data.local.MatchEntity

data class Matches(
    var matches: List<Match>? = null
)

data class Match(
    val team1: String,
    val team2: String,
    var team1Point: Int? = null,
    var team2Point: Int? = null,
) {

    fun toMatchEntity(): MatchEntity {
        return MatchEntity(
            team1 = team1,
            team2 = team2,
            point1 = team1Point ?: 0,
            point2 = team2Point ?: 0,
        )
    }

    fun getPointTeam1(): String {
        if (team1Point == null) return "-"
        return team1Point.toString()
    }

    fun getPointTeam2(): String {
        if (team2Point == null) return "-"
        return team2Point.toString()
    }

    fun hasScore(): Boolean {
        return (team1Point != null || team2Point != null)
    }
}