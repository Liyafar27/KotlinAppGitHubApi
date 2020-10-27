package ru.example.myfirstkotlinapp.api

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path
import retrofit2.http.Query
import ru.example.myfirstkotlinapp.model.Stargazer

interface StargazersInterface {
    @Headers("Accept: application/vnd.github.v3.star+json")
    @GET("/repos/{owner}/{repo}/stargazers?page=1&per_page=1000")
    fun stargazersForRepo(@Path("owner") owner:String,@Path("repo") repo:String,
                          @Query("page") page: Int,
                          @Query("per_page") perPage: Int): Call <List<Stargazer>>
}