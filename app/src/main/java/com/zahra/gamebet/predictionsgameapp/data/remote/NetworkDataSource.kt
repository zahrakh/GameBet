package com.zahra.gamebet.predictionsgameapp.data.remote

import com.zahra.gamebet.common.Either
 import com.zahra.gamebet.predictionsgameapp.data.remote.dto.MatchesDto
import com.zahra.gamebet.predictionsgameapp.data.remote.dto.MatchesResultsDto

interface NetworkDataSource {

    suspend fun getMatches(): Either<MatchesDto, String>

    suspend fun getMatchesResult(): Either<MatchesResultsDto, String>

}