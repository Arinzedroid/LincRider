package com.mobile.app.lincride.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mobile.app.lincride.models.HistoryEntity
import com.mobile.app.lincride.repository.history.HistoryRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MapsViewModel @Inject constructor(private val historyRepo: HistoryRepo): ViewModel() {

    fun createHistory(historyEntity: HistoryEntity){
        viewModelScope.launch {
            historyRepo.addHistory(historyEntity)
        }
    }
}
