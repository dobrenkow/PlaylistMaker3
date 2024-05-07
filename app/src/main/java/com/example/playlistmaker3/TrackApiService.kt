package com.example.playlistmaker3

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object TrackApiService {
    private const val BASE_URL = "https://itunes.apple.com/"
    private val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val create: APICollection = TrackApiService.retrofit.create(APICollection::class.java)
}