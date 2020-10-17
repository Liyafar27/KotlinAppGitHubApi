package ru.example.myfirstkotlinapp.api

import retrofit2.Call
import retrofit2.http.GET

import retrofit2.http.Path
import retrofit2.http.Query

interface GitHubClient {
    @GET("/users/{user}/repos?page=1&per_page=1000")

    fun reposForUser(@Path("user") user: String,
                     @Query("page") page: Int,
                     @Query("per_page") perPage: Int
    ): Call<List<GitHubRepo>>

}
