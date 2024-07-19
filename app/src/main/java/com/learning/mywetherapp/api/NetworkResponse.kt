package com.learning.mywetherapp.api

sealed class NetworkResponse<out T> {

    data class Sucess<out T>(val data: T) : NetworkResponse<T>()
    data class Error(val message: String): NetworkResponse<Nothing>()
    object Loading : NetworkResponse<Nothing>()
}