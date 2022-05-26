package com.zahra.gamebet.predictionsgameapp.data.remote


import com.zahra.gamebet.R
import com.zahra.gamebet.common.Either
import com.zahra.gamebet.predictionsgameapp.data.remote.dto.MatchesDto
import com.zahra.gamebet.predictionsgameapp.data.remote.dto.MatchesResultsDto
import retrofit2.HttpException
import java.io.IOException

class NetworkDataSourceImpl(
    private val api: Api,
    private var stringProvider: StringProvider,
) : NetworkDataSource {


    override suspend fun getMatches(): Either<MatchesDto, String> {
        return try {
            val matches = api.getMatches()
            Either.Success(matches)
        } catch (e: HttpException) {
            Either.Error(error = e.message ?: stringProvider.getString(R.string.error_occurred))
        } catch (e: IOException) {
            Either.Error(error = e.message ?: stringProvider.getString(R.string.check_internet_connection))
        } catch (e: Exception) {
            Either.Error(error = e.message ?: stringProvider.getString(R.string.unknown_error))
        }
    }

    override suspend fun getMatchesResult(): Either<MatchesResultsDto, String> {
        return try {
            val s = api.getMatchesResult()
            Either.Success(s)
        } catch (e: HttpException) {
            Either.Error(error = e.message ?: stringProvider.getString(R.string.error_occurred))
        } catch (e: IOException) {
            Either.Error(error = e.message ?: stringProvider.getString(R.string.check_internet_connection))
        } catch (e: Exception) {
            Either.Error(error = e.message ?: stringProvider.getString(R.string.unknown_error))
        }
    }
}