package com.example.playlistmaker3

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions

class TrackAdapter(private val listTrack: List<Track>) :
    RecyclerView.Adapter<TrackAdapter.TrackViewHolder>() {

    inner class TrackViewHolder(track: View) : RecyclerView.ViewHolder(track) {
        private val artistName: TextView = itemView.findViewById(R.id.listArtistName)
        private val trackName: TextView = itemView.findViewById(R.id.listTrackName)
        private val trackTime: TextView = itemView.findViewById(R.id.trackTime)
        private val artworkUrl100: ImageView = itemView.findViewById(R.id.media)

        fun bind(track: Track) {
            val requestOptions =
                RequestOptions()
                    .placeholder(R.drawable.search_icon)
                    .diskCacheStrategy(
                        DiskCacheStrategy.ALL
                    )

            artistName.text = track.artistName
            trackName.text = track.trackName
            trackTime.text = track.trackTime
            Glide.with(itemView)
                .applyDefaultRequestOptions(requestOptions)
                .load(track.artworkUrl100)
                .transform(CenterCrop(), RoundedCorners(2)).into(artworkUrl100)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrackViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.track_list, parent, false)
        return TrackViewHolder(view)
    }

    override fun getItemCount(): Int = listTrack.size

    override fun onBindViewHolder(holder: TrackViewHolder, position: Int) {
        val item = listTrack[position]
        holder.bind(item)
    }
}