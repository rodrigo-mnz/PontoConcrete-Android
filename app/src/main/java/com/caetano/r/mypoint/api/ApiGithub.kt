package com.caetano.r.mypoint.api

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ApiGithub {

    private val BASE_URL = "https://api.github.com"

    val service: ApiGithubService by lazy {
        initApi()
    }

    private fun initApi(): ApiGithubService {
        val logging = HttpLoggingInterceptor()
        logging.level = HttpLoggingInterceptor.Level.BODY

        val client = OkHttpClient.Builder()
                .addInterceptor(logging)
                .build()

        val mRetrofit = Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build()

        return mRetrofit.create(ApiGithubService::class.java)
    }
}