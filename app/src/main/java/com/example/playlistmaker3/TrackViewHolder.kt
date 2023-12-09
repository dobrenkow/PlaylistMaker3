package com.example.playlistmaker3

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.text.SimpleDateFormat
import java.util.Locale

data class Track(
    val trackName:String,
    val artistName:String,
    val trackTimeMillis:Long,
    val artworkUrl100:String
){
    fun getFormattedTrackTime(): String {
        return SimpleDateFormat("mm:ss", Locale.getDefault()).format(trackTimeMillis)
    }
}

object TrackApiService {
    private const val BASE_URL = "https://itunes.apple.com/"
    val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    val create: APICollection = retrofit.create(APICollection::class.java)
}