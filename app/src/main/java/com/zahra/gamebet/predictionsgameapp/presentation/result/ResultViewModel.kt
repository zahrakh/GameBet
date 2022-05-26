package com.zahra.gamebet.predictionsgameapp.presentation.result

import android.content.SharedPreferences
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zahra.gamebet.common.Either
import com.zahra.gamebet.predictionsgameapp.domain.model.MatchesResultsModel
import com.zahra.gamebet.predictionsgameapp.domain.usecases.GetMatchesResultListUseCase
import com.zahra.gamebet.predictionsgameapp.domain.usecases.ResetMatchUseCase
import com.zahra.gamebet.predictionsgameapp.presentation.list.ARG_MATCH_CURRENT_PAGE
import com.zahra.gamebet.predictionsgameapp.presentation.list.ARG_MATCH_LAST_UPDATE_TIME
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ResultViewModel @Inject constructor(
    private val getMatchesResultListUseCase: GetMatchesResultListUseCase,
    private val resetMatchUseCase: ResetMatchUseCase,
    private val sharedPreferences: SharedPreferences
) : ViewModel() {

    private val _machListResult = MutableLiveData(MatchesResultsModel())
    val machListResult: LiveData<MatchesResultsModel> = _machListResult

    private val _error = MutableStateFlow(false)
    val error = _error.asStateFlow()

    private val _loading = MutableStateFlow(false)
    val loading = _loading.asStateFlow()

    init {
        matchResults()
    }

    fun matchResults(){
        viewModelScope.launch(Dispatchers.IO){
            _error.emit(false)
            _loading.emit(true)

            when (val result = getMatchesResultListUseCase.invoke()) {

                is Either.Success -> {
                    _loading.emit(false)
                    _machListResult.postValue(result.data)
                }
                is Either.Error -> {
                    _loading.emit(false)
                    _error.emit(true)
                }

            }
        }
    }

    fun resetData() {
        sharedPreferences.edit().putLong(ARG_MATCH_LAST_UPDATE_TIME,0).apply()
        sharedPreferences.edit().putInt(ARG_MATCH_CURRENT_PAGE,1).apply()
        viewModelScope.launch(Dispatchers.IO){
            resetMatchUseCase.invoke()
        }
    }

    fun onDestroyView() {
        val editor = sharedPreferences.edit()
        editor.putInt(ARG_MATCH_CURRENT_PAGE,2)
        editor.apply()
    }

}