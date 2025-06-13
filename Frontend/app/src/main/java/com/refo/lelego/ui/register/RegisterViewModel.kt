package com.refo.lelego.ui.register

import androidx.lifecycle.ViewModel
import com.refo.lelego.data.UserRepository

class RegisterViewModel(private val repository: UserRepository) : ViewModel() {
    fun register(email: String, username: String, password: String, role: String) =
        repository.signup(email, username, password, role)
}