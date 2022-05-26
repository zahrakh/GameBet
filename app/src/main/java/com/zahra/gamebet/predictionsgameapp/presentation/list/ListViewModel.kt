package com.zahra.gamebet.predictionsgameapp.presentation.list

import android.content.SharedPreferences
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zahra.gamebet.R
import com.zahra.gamebet.common.Either
import com.zahra.gamebet.predictionsgameapp.domain.model.Match
import com.zahra.gamebet.predictionsgameapp.domain.model.Matches
import com.zahra.gamebet.predictionsgameapp.domain.model.Point
import com.zahra.gamebet.predictionsgameapp.domain.usecases.GetMatchesListUseCase
import com.zahra.gamebet.predictionsgameapp.domain.usecases.UpdateMatchUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ListViewModel @Inject constructor(
    private val getMatchesListUseCase: GetMatchesListUseCase,
    private val updateMatchUseCase: UpdateMatchUseCase,
    private val sharedPreferences: SharedPreferences
) : ViewModel() {

    private val _machList = MutableLiveData(Matches())
    val machList: LiveData<Matches> = _machList

    private val _error = MutableStateFlow(false)
    val error = _error.asStateFlow()

    private val _loading = MutableStateFlow(false)
    val loading = _loading.asStateFlow()

    private val _showResult = MutableLiveData(false)
    val showResult: LiveData<Boolean> = _showResult

    private val _errorMessageRes = MutableLiveData<Int?>()
    val errorMessageRes: LiveData<Int?> = _errorMessageRes

    fun init() {
        if (sharedPreferences.getInt(ARG_MATCH_CURRENT_PAGE, 1) == 2) {
            _showResult.postValue(true)
        } else {
            getMatchList()
        }
    }

    fun getMatchList() {
        viewModelScope.launch(Dispatchers.IO) {
            _error.emit(false)
            _loading.emit(true)
            when (val result = getMatchesListUseCase.invoke()) {
                is Either.Success -> {
                    _loading.emit(false)
                    _machList.postValue(result.data)
                }
                is Either.Error -> {
                    _loading.emit(false)
                    _error.emit(true)
                }
            }

        }
    }

    fun updateMatch(point: Point?) {
        if (point?.id == null || (machList.value?.matches?.size ?: 0) < 0) return
        try {
            viewModelScope.launch(Dispatchers.IO) {
                _machList.value.let {
                    it?.matches?.get(point.id)?.team1Point = point.team1Point
                    it?.matches?.get(point.id)?.team2Point = point.team2Point
                    _machList.postValue(it)
                    updateMatchUseCase.invoke(
                        Match(
                            team1 = point.team1Name,
                            team2 = point.team2Name,
                            team1Point = point.team1Point,
                            team2Point = point.team2Point,
                        )
                    )
                }
            }
        } catch (e: Exception) {
            //todo report crash
        }
    }

    fun onDestroyView() {
        val editor = sharedPreferences.edit()
        editor.putInt(ARG_MATCH_CURRENT_PAGE, 1)
        editor.apply()
    }

    fun openResultPage() {
        if ((_machList.value?.matches?.filter { it.hasScore() }?.size ?: 0) > 0) {
            _showResult.postValue(true)
        } else {
            _errorMessageRes.postValue(R.string.make_prediction_alert)
        }
    }

    fun clearResult() {
        _showResult.postValue(false)
    }


}