package com.zahra.gamebet.predictionsgameapp.data.remote.dto

import com.google.gson.annotations.SerializedName
import com.zahra.gamebet.predictionsgameapp.domain.model.MatchResultModel
import com.zahra.gamebet.predictionsgameapp.domain.model.MatchesResultsModel

data class MatchesResultsDto(
    @SerializedName("matches")
    var matchesResult: List<MatchResultDto>? = null
) {
    fun toMatchesResult(): MatchesResultsModel {
        return MatchesResultsModel(
            matchesResult = matchesResult?.map { it.toMatchResult() }
        )
    }
}

data class MatchResultDto(
    @SerializedName("team1") var team1: String? = null,
    @SerializedName("team2") var team2: String? = null,
    @SerializedName("team1_points") var team1Points: Int? = null,
    @SerializedName("team2_points") var team2Points: Int? = null
) {
    fun toMatchResult(): MatchResultModel = MatchResultModel(
        team1 = team1,
        team2 = team2,
        team1Points = team1Points,
        team2Points = team2Points
    )
}