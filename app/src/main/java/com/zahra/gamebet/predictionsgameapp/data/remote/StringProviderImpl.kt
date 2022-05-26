package com.zahra.gamebet.predictionsgameapp.data.remote

import android.content.Context
import androidx.annotation.StringRes

class StringProviderImpl(
    private val appContext: Context,
) : StringProvider {
    override fun getString(@StringRes id: Int): String = appContext.getString(id)
}