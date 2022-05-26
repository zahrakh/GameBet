package com.zahra.gamebet.predictionsgameapp.domain.usecases

import com.zahra.gamebet.predictionsgameapp.domain.repository.GameRepository

class ResetMatchUseCase (
    private val repository: GameRepository,
) {
    suspend operator fun invoke()  {
       return repository.resetMatch()
    }
}


