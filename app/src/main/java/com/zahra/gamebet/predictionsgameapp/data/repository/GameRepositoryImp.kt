package com.zahra.gamebet.predictionsgameapp.data.repository

import android.content.SharedPreferences
import com.zahra.gamebet.common.Either
import com.zahra.gamebet.predictionsgameapp.data.local.MatchDao
import com.zahra.gamebet.predictionsgameapp.data.local.MatchEntity
import com.zahra.gamebet.predictionsgameapp.data.local.MatchesDao
import com.zahra.gamebet.predictionsgameapp.data.remote.NetworkDataSource
import com.zahra.gamebet.predictionsgameapp.domain.model.Match
import com.zahra.gamebet.predictionsgameapp.domain.model.Matches
import com.zahra.gamebet.predictionsgameapp.domain.model.MatchesResultsModel
import com.zahra.gamebet.predictionsgameapp.domain.repository.GameRepository
import com.zahra.gamebet.predictionsgameapp.presentation.list.ARG_MATCH_LAST_UPDATE_TIME
import javax.inject.Inject

class GameRepositoryImp @Inject constructor(
    private val networkDataSource: NetworkDataSource,
    private val matchesDao: MatchesDao,
    private val sharedPreferences: SharedPreferences
) : GameRepository {


    override suspend fun getMatches(): Either<Matches, String> {
        if (isMoreThanOneMinutes()) {
            return when (val result = networkDataSource.getMatches()) {
                is Either.Success -> {
                    sharedPreferences
                        .edit()
                        .putLong(ARG_MATCH_LAST_UPDATE_TIME, System.currentTimeMillis())

                    //save to db
                    matchesDao.delete()
                    result.data.toMatches().matches?.map {
                        it.toMatchEntity()
                    }?.let { matchesDao.insertMatchesEntity(it) }

                    Either.Success(result.data.toMatches())
                }
                is Either.Error -> {
                    Either.Error(result.error)
                }
            }
        } else {
            val list = matchesDao.getMatchEntities().map { it.toMatch() }
            return Either.Success(Matches(list))
        }

    }

    override suspend fun getMatchesResult(): Either<MatchesResultsModel, String> {
        return when (val result = networkDataSource.getMatchesResult()) {
            is Either.Success -> {
                val predictionMatch = matchesDao.getMatchEntities().map { it.toMatch() }
                val r = result.data.toMatchesResult()
                r.matchesResult?.forEach { matchResult ->
                    predictionMatch.forEach { match ->
                        if (matchResult.team1 == match.team1 && matchResult.team2 == match.team2) {
                            matchResult.team1PointsPrediction = match.team1Point
                            matchResult.team2PointsPrediction = match.team2Point
                        }
                    }
                }
                Either.Success(MatchesResultsModel(r.matchesResult))
            }
            is Either.Error -> {
                Either.Error(result.error)
            }
        }
    }

    override fun resetMatch() {
        matchesDao.reset()
    }

    override suspend fun updateMatch(match: Match) {
        matchesDao.update(
            team1name = match.team1,
            team2name = match.team2,
            point1 = match.team1Point,
            point2 = match.team2Point
        )
    }


    private fun isMoreThanOneMinutes(): Boolean {
        val l = sharedPreferences.getLong(ARG_MATCH_LAST_UPDATE_TIME, 0L)
        if (l == 0L) return true
        val seconds = (System.currentTimeMillis() - l) / 1000
        val minutes = seconds / 60
        return minutes > 1
    }

}