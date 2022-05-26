package com.zahra.gamebet.predictionsgameapp.domain.usecases

import com.zahra.gamebet.predictionsgameapp.domain.model.Match
import com.zahra.gamebet.predictionsgameapp.domain.repository.GameRepository

class UpdateMatchUseCase (
    private val repository: GameRepository,
) {
    suspend operator fun invoke(match: Match)  {
       return repository.updateMatch(match)
    }
}


