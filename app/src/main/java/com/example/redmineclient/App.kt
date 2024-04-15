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

        fun getAuthData(): Pair<String?, String?> {
            return Pair(prefs.getString("login", null), prefs.getString("password", null))
        }

        fun setAuthData(login: String, password: String) {
            prefs.edit().putString("login", login).apply()
            prefs.edit().putString("password", password).apply()
            Log.d("my", "App: Set new login: \'$login\', password: \'$password\'")
        }

        // for debug
//        fun clearPrefs() {
//            prefs.edit().clear().apply()
//        }
    }
}