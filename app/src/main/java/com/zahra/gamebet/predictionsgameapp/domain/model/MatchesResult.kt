package com.zahra.gamebet.predictionsgameapp.domain.model

data class MatchesResultsModel(
    var matchesResult: List<MatchResultModel>? = null
)

data class MatchResultModel(
    var team1: String? = null,
    var team2: String? = null,
    var team1Points: Int? = null,
    var team2Points: Int? = null,
    var team1PointsPrediction: Int? = null,
    var team2PointsPrediction: Int? = null
){
    fun getActualPointTeam1():String{
        if (team1Points==null) return "-"
        return team1Points.toString()
    }
    fun getActualPointTeam2():String{
        if (team2Points==null) return "-"
        return team2Points.toString()
    }
    fun getPredictionPointTeam1():String{
        if (team1PointsPrediction==null) return "-"
        return team1PointsPrediction.toString()
    }
    fun getPredictionPointTeam2():String{
        if (team2PointsPrediction==null) return "-"
        return team2PointsPrediction.toString()
    }
}