package com.zahra.gamebet.predictionsgameapp.data.remote.dto

import com.google.gson.annotations.SerializedName
import com.zahra.gamebet.predictionsgameapp.domain.model.Match
import com.zahra.gamebet.predictionsgameapp.domain.model.Matches


data class MatchesDto(
    @SerializedName("matches")
    var matches: List<MatchDto>? = null
) {
    fun toMatches(): Matches {
        return Matches(
            matches = matches?.map { it.toMatch() }
        )
    }
}

data class MatchDto(
    @SerializedName("team1")
    private val team1: String,
    @SerializedName("team2")
    private val team2: String
) {
    fun toMatch(): Match = Match(team1 = team1, team2 = team2)
}