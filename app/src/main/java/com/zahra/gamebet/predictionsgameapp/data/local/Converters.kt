package com.zahra.gamebet.predictionsgameapp.data.local

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.zahra.gamebet.predictionsgameapp.domain.model.Match
import com.zahra.gamebet.predictionsgameapp.domain.model.Matches

class Converters {

    @TypeConverter
    fun listToString(list: List<MatchEntity>) = Gson().toJson(list)

    @TypeConverter
    fun stringToList(value: String): List<MatchEntity> {
        val type = object : TypeToken<List<MatchEntity>>() {}.type
        return Gson().fromJson(value, type)
    }
}