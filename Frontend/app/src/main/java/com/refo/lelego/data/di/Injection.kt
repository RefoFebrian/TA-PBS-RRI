package com.refo.lelego.data.di

import android.content.Context
import com.refo.lelego.data.UserRepository
import com.refo.lelego.data.pref.UserPreferences
import com.refo.lelego.data.pref.dataStore
import com.refo.lelego.data.retrofit.ApiConfig
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking

object Injection {
    fun provideRepository(context: Context): UserRepository {
        val pref = UserPreferences.getInstance(context.dataStore)
        val user = runBlocking { pref.getSession().first() }
        val apiService = ApiConfig.getApiService(user.token)
        return UserRepository.getInstance(apiService, pref)
    }
}