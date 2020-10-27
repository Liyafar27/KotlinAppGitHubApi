package ru.example.myfirstkotlinapp.network

import com.squareup.moshi.Moshi
import com.squareup.moshi.adapters.Rfc3339DateJsonAdapter
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

import java.util.*

object APIClient {

    val moshi = Moshi.Builder()
        .add(Date::class.java, Rfc3339DateJsonAdapter().nullSafe())
        .build()

    val retrofit = Retrofit.Builder()
        .client(getHttpClient().build())
        .baseUrl("https://api.github.com/")
        .addConverterFactory(MoshiConverterFactory.create(moshi))
        .build()


    fun getHttpClient(): OkHttpClient.Builder {

        val logging = HttpLoggingInterceptor()
        val client1 = OkHttpClient.Builder()
            .addInterceptor(logging.setLevel(HttpLoggingInterceptor.Level.BODY))
        return client1
    }
}