package com.example.redmineclient

import android.app.Application
import android.content.SharedPreferences
import android.util.Log
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class App : Application() {
    override fun onCreate() {
        super.onCreate()
        prefs = getSharedPreferences("auth", MODE_PRIVATE)
        prefs.edit().putString("0", "maxim.danilichenko").apply()
        prefs.edit().putString("1", "Jackp0t For A Sucker 1337").apply()
    }

    companion object{
        private lateinit var prefs: SharedPreferences

        fun getAuthData(): Pair<String, String> {
            return Pair(prefs.getString("0", "null")!!, prefs.getString("1", "null")!!)
        }

        fun setAuthData(login: String, password: String) {
            prefs.edit().putString("0", login).putString("1", password).apply()
        }

        fun clearPrefs() {
            prefs.edit().clear().apply()
        }
    }
}