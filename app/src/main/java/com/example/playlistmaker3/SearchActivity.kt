package com.example.playlistmaker3

import SharedPreferenceConverter
import android.content.Context
import android.content.SharedPreferences
import android.content.res.Configuration
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doOnTextChanged
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.playlistmaker3.databinding.ActivitySearchBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

private const val NEW_TRACK = "new_track"
private const val MAIN_KEY = "main_key"
private const val PREF = "pref_data"


class SearchActivity : AppCompatActivity() {

    private var savedText: String = ""
    private var binding: ActivitySearchBinding? = null
    private lateinit var listener: SharedPreferences.OnSharedPreferenceChangeListener
    private var listTrack = mutableListOf<Track>()
    private lateinit var prefData: SharedPreferences
    private val trackAdapter = TrackHistoryAdapter(this@SearchActivity)


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(binding?.root)
        binding?.search?.onFocusChangeListener
        binding?.clearButton?.visibility = View.INVISIBLE
        binding?.toolbarSettings?.setNavigationOnClickListener { onBackPressed() }
        prefData = getSharedPreferences(PREF, MODE_PRIVATE)

        if (savedInstanceState != null) {
            savedText = savedInstanceState.getString((getString(R.string.search_text)), "")
            binding?.search?.setText(savedText)
        }
        binding?.search?.setOnFocusChangeListener { _, hasFocus ->
            if ((hasFocus) && (trackAdapter.itemCount > 0)) {
                showButtonClear(true)
            }
        }
    }

    override fun onStart() {
        super.onStart()
        getDataTrack()
        showAdapterHistory()
        getDataForTrack()
        prefData.registerOnSharedPreferenceChangeListener(listener)
        showHistoryRequest()
        searchButton()
        updateData()
        clearSharedPreference()
        textWatcher()

    }

    override fun onStop() {
        super.onStop()
        val pref = SharedPreferenceWorkPlace
        pref.saveDataTrack(prefData, listTrack, MAIN_KEY)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        savedText = binding?.search?.text.toString()
        outState.putString((getString(R.string.search_text)), savedText)
    }

    private fun getDataTrack() {
        val sharedPreferencesConverter = SharedPreferenceConverter
        val tracks = prefData.getString(MAIN_KEY, null)
        if (tracks != null) {
            listTrack = sharedPreferencesConverter.createTracksListFromJson(tracks).toMutableList()
        }
    }

    private fun showAdapterHistory() {
        val trackAdapter = trackAdapter
        trackAdapter.update(listTrack)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        savedText = savedInstanceState.getString((getString(R.string.search_text)), "")
        binding?.search?.setText(savedText)
    }

    private fun textWatcher() {
        val myTextWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                binding?.clearButton?.visibility = View.INVISIBLE
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (s.isNullOrEmpty()) {
                    binding?.clearButton?.visibility = View.GONE
                } else {
                    binding?.clearButton?.visibility = View.VISIBLE
                    showButtonClear(show = false)
                }
            }

            override fun afterTextChanged(s: Editable?) {}
        }
        binding?.search?.doOnTextChanged { text, _, _, _ ->
            myTextWatcher.onTextChanged(text, 0, 0, 0)

        }

        binding?.clearButton?.setOnClickListener {
            binding?.search?.text?.clear()
            hideKeyboard()
            hidePicture()
            binding?.apply {
                btnMessage.visibility = View.INVISIBLE
            }
            if (listTrack.isEmpty()) {
                showButtonClear(false)
            } else {
                showButtonClear(true)
                showHistoryRequest()
            }
        }
    }

    private fun hideKeyboard() {
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(binding?.clearButton?.windowToken, 0)
    }

    private fun hidePicture() {
        binding?.apply {
            ivMessage.visibility = View.INVISIBLE
            tvMessage.visibility = View.INVISIBLE
            btnMessage.visibility = View.INVISIBLE
        }
    }

    private fun showButtonClear(show: Boolean) {
        if (show) {
            binding?.apply {
                btnClearHistory.visibility = View.VISIBLE
                tvHint.visibility = View.VISIBLE
                trackRecyclerView.visibility = View.VISIBLE
                trackRecyclerView.layoutManager = LinearLayoutManager(this@SearchActivity)
                trackRecyclerView.adapter = trackAdapter
            }
        } else {
            binding?.apply {
                btnClearHistory.visibility = View.INVISIBLE
                tvHint.visibility = View.GONE
                trackRecyclerView.visibility = View.INVISIBLE
                trackRecyclerView.layoutManager = LinearLayoutManager(this@SearchActivity)
                trackRecyclerView.adapter = trackAdapter
            }
            trackAdapter.clearListAdapter()
        }
    }


    private fun clearAdapter() {
        val clearAdapter = TrackAdapter(this, prefData)
        clearAdapter.clearListAdapter()
        binding?.trackRecyclerView?.adapter = clearAdapter
    }

    private fun updateData() {
        binding?.btnMessage?.setOnClickListener { getWebRequest() }
    }

    private fun searchButton() {
        binding?.search?.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                showButtonClear(true)
                binding?.btnClearHistory?.visibility = View.INVISIBLE
                binding?.tvHint?.visibility = View.GONE
                clearAdapter()
                getWebRequest()
            }
            return@setOnEditorActionListener false
        }
    }

    private fun getWebRequest() {
        val query = binding?.search?.text.toString().trim()
        val apiService = TrackApiService.create
        apiService.search(query).enqueue(object : Callback<ResponseTrack> {
            override fun onResponse(
                call: Call<ResponseTrack>,
                response: Response<ResponseTrack>,
            ) {
                handleResponse(response)
            }

            override fun onFailure(call: Call<ResponseTrack>, t: Throwable) {
                handleFailure()
            }
        })
    }


    private fun handleResponse(response: Response<ResponseTrack>) {
        val trackList = response.body()?.results ?: emptyList()

        if (response.isSuccessful && trackList.isNotEmpty()) {
            binding?.apply {
                trackRecyclerView.visibility = View.VISIBLE
                ivMessage.visibility = View.INVISIBLE
                btnMessage.visibility = View.INVISIBLE
                tvMessage.text = ""
                binding?.search?.visibility = View.VISIBLE
            }

            val trackAdapter =
                TrackAdapter(this@SearchActivity, prefData)
            binding?.trackRecyclerView?.layoutManager = LinearLayoutManager(this@SearchActivity)
            binding?.trackRecyclerView?.adapter = trackAdapter
            trackAdapter.updateData(trackList)

        } else {
            binding?.apply {
                trackRecyclerView.visibility = View.INVISIBLE
                ivMessage.visibility = View.VISIBLE
                tvMessage.visibility = View.VISIBLE
                btnMessage.visibility = View.INVISIBLE
            }

            if (response.isSuccessful) {
                binding?.tvMessage?.text = getString(R.string.no_content)
                binding?.ivMessage?.setImageResource(
                    if (isNightModeEnabled()) R.drawable.no_content_dark
                    else R.drawable.no_content
                )
            } else {
                binding?.tvMessage?.text = getString(R.string.no_web)
                binding?.ivMessage?.setImageResource(
                    if (isNightModeEnabled()) R.drawable.no_content_dark
                    else R.drawable.no_content
                )
            }
        }
    }

    private fun handleFailure() {
        binding?.apply {
            trackRecyclerView.visibility = View.INVISIBLE
            ivMessage.visibility = View.VISIBLE
            tvMessage.visibility = View.VISIBLE
            btnMessage.visibility = View.VISIBLE
            tvMessage.text = getString(R.string.no_web)
            ivMessage.setImageResource(
                if (isNightModeEnabled()) R.drawable.no_web_dark
                else R.drawable.no_web
            )
        }
    }

    private fun isNightModeEnabled(): Boolean {
        val currentNightMode = resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK
        return currentNightMode == Configuration.UI_MODE_NIGHT_YES
    }


    private fun clearSharedPreference() {
        binding?.btnClearHistory?.setOnClickListener {
            prefData.edit().clear().apply()
            trackAdapter.clearListAdapter()
            listTrack.clear()
            showButtonClear(false)
        }
    }

    private fun showHistoryRequest() {
        trackAdapter.update(listTrack)
    }

    private fun getDataForTrack() {
        val sharedPreferenceConverter = SharedPreferenceConverter
        fun addTrack(track: String) {
            val createTrack = sharedPreferenceConverter.createTrackFromJson(track)
            if (listTrack.contains(createTrack)) {
                listTrack.remove(createTrack)
            }
            listTrack.add(0, createTrack)
            trackAdapter.notifyItemInserted(0)
        }
        listener = SharedPreferences.OnSharedPreferenceChangeListener { sharedPreferences, key ->
            if (key == NEW_TRACK) {
                val track = sharedPreferences?.getString(NEW_TRACK, null)
                if (listTrack.size < 10) {
                    if (track != null) {
                        addTrack(track)
                    }
                } else {
                    listTrack.removeLast()
                    addTrack(track!!)
                }
            }
        }
    }
}

