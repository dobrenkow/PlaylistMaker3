package com.example.playlistmaker3

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface APICollection {
    @GET("search?entity=song")
    fun search(@Query("term") text:String) : Call<ResponseTrack>
}