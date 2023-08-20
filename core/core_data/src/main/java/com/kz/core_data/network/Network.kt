package com.kz.core_data.network

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import retrofit2.Response

fun <T> flowOf(
    call: suspend () -> Response<T>?,
    dispatcher: CoroutineDispatcher = Dispatchers.IO
): Flow<NetworkResult<T>?> {
    return flow {
        emit(NetworkResult.InProgress)

        call()?.let { response ->
            try {
                if (response.isSuccessful) {
                    response.body()?.let {
                        emit(NetworkResult.Success(it))
                    }
                } else {
                    response.errorBody()?.let {
                        val error = it.string()
                        it.close()
                        emit(NetworkResult.Error(error))
                    }
                }
            } catch (e: Exception) {
                emit(NetworkResult.Error(e.toString()))
            }
        }

    }.flowOn(dispatcher)
}