package com.refo.lelego.ui.login

import androidx.lifecycle.ViewModel
import com.refo.lelego.data.UserRepository
import com.refo.lelego.data.pref.UserModel

class LoginViewModel(private val repository: UserRepository) : ViewModel() {
    fun login(username: String, password: String) = repository.signin(username, password)

    suspend fun saveSession(userModel: UserModel) {
        repository.saveSession(userModel)
    }
}