package com.kz.core_data.network

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object NetworkBuilder {

    fun <T> createModule(url: String, service: Class<T>): T = Retrofit.Builder()
        .baseUrl(url)
        .client(
            OkHttpClient
                .Builder()
                .addInterceptor(logInterceptor())
                .build()
        )
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(service)

    private fun logInterceptor() = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }
}