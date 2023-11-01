package com.example.playlistmaker3

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.ImageButton
import androidx.appcompat.widget.Toolbar
import androidx.core.widget.doOnTextChanged



class SearchActivity : AppCompatActivity() {
    private lateinit var toolbarSettings: Toolbar
    private lateinit var search: EditText
    private lateinit var button: ImageButton
    private var savedText: String = ""
    companion object {
        const val search_text = "search_text"
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
       //
}