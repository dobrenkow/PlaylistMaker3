package com.example.playlistmaker3

import SharedPreferenceConverter
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

class TrackAdapter(private val context: Context, private val pref: SharedPreferences) :
    RecyclerView.Adapter<TrackViewHolder>() {

    private var listTrack: List<Track> = emptyList()
    private val newTrack = "new_track"
    private val sharedPreferenceConverter = SharedPreferenceConverter

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrackViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.track_list, parent, false)
        return TrackViewHolder(view, context)
    }

    override fun getItemCount(): Int = listTrack.size


    override fun onBindViewHolder(holder: TrackViewHolder, position: Int) {
        val item = listTrack[position]
        holder.bind(item)
        holder.itemView.setOnClickListener {
            write(item)
            holder.bind(item)
            val itemJson = SharedPreferenceConverter.createJsonFromTrack(item)
            val intent = Intent(context, MediaActivity::class.java)
            intent.putExtra(Constant.KEY, itemJson)
            context.startActivity(intent)
            Log.d("mes", itemJson)
        }
    }

    fun updateData(newListTrack: List<Track>) {
        listTrack = newListTrack
        notifyDataSetChanged()
    }

    fun clearListAdapter() {
        listTrack = emptyList()
        notifyDataSetChanged()
    }

    private fun write(track: Track) {
        pref.edit()
            .putString(newTrack, sharedPreferenceConverter.createJsonFromTrack(track))
            .apply()
        Log.d("write", track.toString())
    }
}
