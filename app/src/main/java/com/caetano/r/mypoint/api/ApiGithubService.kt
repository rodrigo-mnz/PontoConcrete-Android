package com.caetano.r.mypoint.api

import retrofit2.Call
import retrofit2.http.GET

interface ApiGithubService {

    @GET("/repos/rodrigo-mnz/PontoConcrete-Android/releases/latest")
    fun getLatestRelease(): Call<GithubRelease>
}