package com.refo.lelego.data

sealed class ResultAnalyze<out R> private constructor() {
    data class Success<out T>(val data: T) : ResultAnalyze<T>()
    data class Error(val error: String) : ResultAnalyze<Nothing>()
    object Loading : ResultAnalyze<Nothing>()
}