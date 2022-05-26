package com.zahra.gamebet.predictionsgameapp.domain.repository

import com.zahra.gamebet.common.Either
import com.zahra.gamebet.predictionsgameapp.data.remote.dto.MatchesDto
import com.zahra.gamebet.predictionsgameapp.data.remote.dto.MatchesResultsDto
import com.zahra.gamebet.predictionsgameapp.domain.model.Match
import com.zahra.gamebet.predictionsgameapp.domain.model.Matches
import com.zahra.gamebet.predictionsgameapp.domain.model.MatchesResultsModel

interface GameRepository {

    suspend fun updateMatch(match:Match)

    suspend fun getMatches(): Either<Matches, String>

    suspend fun getMatchesResult(): Either<MatchesResultsModel, String>

    abstract fun resetMatch()

}