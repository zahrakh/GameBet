package com.zahra.gamebet.predictionsgameapp.domain.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Point(
    val id:Int,
    var team1Name: String,
    var team1Point: Int,
    var team2Name: String,
    var team2Point: Int,
) : Parcelable {

    fun increaseTeam1Point() {
        team1Point+=1
    }

    fun increaseTeam2Point() {
       team2Point+=1
    }

    fun decreaseTeam1Point() :Boolean{
        if (team1Point==0) return false
        team1Point-=1
        return true
    }

    fun decreaseTeam2Point() :Boolean{
        if (team2Point==0) return false
        team2Point-=1
        return true
    }
}