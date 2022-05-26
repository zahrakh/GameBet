package com.zahra.gamebet.predictionsgameapp.data.remote

import androidx.annotation.StringRes

interface StringProvider {

    fun getString(@StringRes id: Int): String

}