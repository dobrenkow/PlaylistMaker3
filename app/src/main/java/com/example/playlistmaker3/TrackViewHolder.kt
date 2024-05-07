package com.example.playlistmaker3

import android.content.Context
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView


 class TrackViewHolder(track: View,private val context: Context) : RecyclerView.ViewHolder(track) {
        private val artistName: TextView = itemView.findViewById(R.id.listArtistName)
        private val trackName: TextView = itemView.findViewById(R.id.listTrackName)
        private val trackTime: TextView = itemView.findViewById(R.id.trackTime)
        private val artworkUrl100: ImageView = itemView.findViewById(R.id.media)

        fun bind(track: Track) {
            artistName.text = track.artistName
            trackName.text = track.trackName
            trackTime.text = track.getFormattedTrackTime()
            ImageWorkPlace.getRecomendationImage(context,false,artworkUrl100,track)
    }
}