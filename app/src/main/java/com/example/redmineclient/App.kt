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
    }

    companion object{
        private lateinit var prefs: SharedPreferences

        fun getAuthData(): Pair<String, String> {
            return Pair(prefs.getString("0", "null")!!, prefs.getString("1", "null")!!)
        }

        fun setAuthData(login: String, password: String) {
            prefs.edit().putString("0", login).apply()
            prefs.edit().putString("1", password).apply()
            Log.d("my", "App: Set new login: \'$login\', password: \'$password\'")
        }

        fun clearPrefs() {
            prefs.edit().clear().apply()
        }
    }
}