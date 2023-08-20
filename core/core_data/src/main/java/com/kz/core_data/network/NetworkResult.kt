package com.kz.core_data.network

sealed class NetworkResult<out T> {
    data class Success<out T>(val data: T) : NetworkResult<T>()
    data class Error(val message: String? = null) :
        NetworkResult<Nothing>()

    object InProgress : NetworkResult<Nothing>()
}