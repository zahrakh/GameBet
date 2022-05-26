package com.zahra.gamebet.predictionsgameapp.domain.usecases

import com.zahra.gamebet.common.Either
import com.zahra.gamebet.predictionsgameapp.data.remote.dto.MatchesDto
import com.zahra.gamebet.predictionsgameapp.domain.model.Matches
import com.zahra.gamebet.predictionsgameapp.domain.repository.GameRepository

class GetMatchesListUseCase (
    private val repository: GameRepository,
) {
    suspend operator fun invoke(): Either<Matches, String> {
       return repository.getMatches()
    }
}


