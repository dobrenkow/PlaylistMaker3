package com.example.playlistmaker3

import android.app.Activity
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.Switch
import android.widget.Toolbar
import androidx.appcompat.app.AppCompatDelegate

class SettingsActivity : AppCompatActivity() {

    private lateinit var toolbar: Toolbar
    private lateinit var shareButton: Button
    private lateinit var buttonWriteSupport: Button
    private lateinit var buttonUserAgreement: Button
    //private lateinit var themeSwitch: Switch

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        shareButton = findViewById(R.id.share_app_button)
        buttonWriteSupport = findViewById(R.id.support_button)
        buttonUserAgreement = findViewById(R.id.terms_button)
        //themeSwitch = findViewById(R.id.dark_theme_switch)
        val isDarkMode = AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES
        //themeSwitch.isChecked = isDarkMode
        //setUpToolbar()
        onClickListenerButton()
    }

    private fun shareText(text: String, activity: Activity) {
        val sendIntent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, text)
            type = getString(R.string.type_share_text)
        }

        val shareIntent = Intent.createChooser(sendIntent, null)
        activity.startActivity(shareIntent)
    }

    private fun openEmailApp() {
        val message = getString(R.string.email_message)
        val theme = getString(R.string.email_theme)
        Intent(Intent.ACTION_SENDTO).apply {
            data = Uri.parse(getString(R.string.url_mail))
            putExtra(Intent.EXTRA_EMAIL, arrayOf(getString(R.string.my_email)))
            putExtra(Intent.EXTRA_SUBJECT, theme)
            putExtra(Intent.EXTRA_TEXT, message)
            startActivity(this)
        }
    }

    private fun parseWeb() {
        val url = getString(R.string.web_url)
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
        startActivity(intent)
    }

    private fun onClickListenerButton() {
        shareButton.setOnClickListener {
            shareText(getString(R.string.web_document), this)
        }
        buttonWriteSupport.setOnClickListener {
            openEmailApp()
        }
        buttonUserAgreement.setOnClickListener {
            parseWeb()
        }
        /* themeSwitch.setOnCheckedChangeListener { _, isChecked ->
             toggleTheme(isChecked)
         }

     private fun setUpToolbar() {
             toolbar = findViewById(R.id.toolbarSettings)
             toolbar.setNavigationOnClickListener { onBackPressed() } */

    }
}