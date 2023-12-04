package com.example.playlistmaker3
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

class TrackAdapter(private val context: Context) :
    RecyclerView.Adapter<TrackView>() {
    private var listTrack: List<Track> = emptyList()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrackView {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.track_list, parent, false)
        return TrackView(view, context)
    }

    override fun getItemCount(): Int = listTrack.size
    override fun onBindViewHolder(holder: TrackView, position: Int) {
        val item = listTrack[position]
        holder.bind(item)
    }

    fun updateData(newListTrack: List<Track>) {
        listTrack = newListTrack
        notifyDataSetChanged()
    }

    fun clearListAdapter() {
        listTrack = emptyList()
        notifyDataSetChanged()
    }

}