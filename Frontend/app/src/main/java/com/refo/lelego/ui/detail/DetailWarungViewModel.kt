package com.refo.lelego.ui.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.refo.lelego.data.ResultAnalyze
import com.refo.lelego.data.UserRepository
import com.refo.lelego.data.response.WarungDetailData

class DetailWarungViewModel(private val repository: UserRepository) : ViewModel() {
    fun getWarungDetail(id: Int): LiveData<ResultAnalyze<WarungDetailData>> {
        return repository.getWarungDetail(id)
    }
}