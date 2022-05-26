package com.zahra.gamebet.predictionsgameapp.data.remote

import com.zahra.gamebet.predictionsgameapp.data.remote.dto.MatchesDto
import com.zahra.gamebet.predictionsgameapp.data.remote.dto.MatchesResultsDto
import retrofit2.http.GET

interface Api {

    companion object {
        const val BASE_URL = "https://run.mocky.io/"
    }

    @GET("v3/61781ef2-e553-4282-a590-6b8f76cc98c9")
    suspend fun getMatches(): MatchesDto

    @GET("v3/c9550379-45f1-4287-bc93-0d85ed88bd85")
    suspend fun getMatchesResult(): MatchesResultsDto


}