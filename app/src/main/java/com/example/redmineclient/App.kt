package com.example.redmineclient

import android.app.Application
import android.content.SharedPreferences
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class App : Application() {
    override fun onCreate() {
        super.onCreate()
        prefs = getSharedPreferences("auth", MODE_PRIVATE)
    }

    companion object{
        private lateinit var prefs: SharedPreferences
        fun getPrefs(): Map<String, String> {
            val result = mapOf<String, String>()
            return result
        }

        fun setPrefs(login: String, password: String) {
            prefs.edit().putString(login, password).apply()
        }

        fun clearPrefs() {
            prefs.edit().clear().apply()
        }
    }
}