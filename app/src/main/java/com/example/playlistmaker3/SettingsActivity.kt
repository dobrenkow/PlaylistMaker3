package com.example.playlistmaker3

import android.app.Activity
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.Switch
import androidx.appcompat.widget.Toolbar
import androidx.appcompat.app.AppCompatDelegate



class SettingsActivity : AppCompatActivity() {

    private lateinit var toolbarSettings: Toolbar
    private lateinit var shareButton: Button
    private lateinit var buttonWriteSupport: Button
    private lateinit var buttonUserAgreement: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

       //

}