package com.zahra.gamebet.predictionsgameapp.domain.usecases

import com.zahra.gamebet.common.Either
import com.zahra.gamebet.predictionsgameapp.data.remote.dto.MatchesResultsDto
import com.zahra.gamebet.predictionsgameapp.domain.model.MatchesResultsModel
import com.zahra.gamebet.predictionsgameapp.domain.repository.GameRepository

class GetMatchesResultListUseCase(
    private val repository: GameRepository,
) {
    suspend operator fun invoke(): Either<MatchesResultsModel, String> {
      return  repository.getMatchesResult()
    }
}

