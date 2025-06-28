package com.refo.lelego.ui.profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.refo.lelego.data.UserRepository
import com.refo.lelego.data.pref.UserModel

class ProfileViewModel(private val repository: UserRepository) : ViewModel() {
    val userSession: LiveData<UserModel> = repository.getSession().asLiveData()

    suspend fun logout() {
        repository.logout()
    }
}