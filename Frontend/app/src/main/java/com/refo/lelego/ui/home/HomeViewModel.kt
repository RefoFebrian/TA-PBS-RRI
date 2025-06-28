package com.refo.lelego.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.refo.lelego.data.ResultAnalyze
import com.refo.lelego.data.UserRepository
import com.refo.lelego.data.response.DataItem
import com.refo.lelego.data.response.WarungDetailData

class HomeViewModel(private val repository: UserRepository) : ViewModel() {
    val warung: LiveData<ResultAnalyze<List<WarungDetailData>>> = repository.getWarungAll()
}