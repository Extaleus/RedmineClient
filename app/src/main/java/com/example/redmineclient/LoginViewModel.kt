package com.example.redmineclient

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class LoginViewModel: ViewModel() {
    private lateinit var redmineRequest: RedmineRequest

    fun loginRequest(username: String, password: String){
        viewModelScope.launch(Dispatchers.IO) {
            redmineRequest =
                Retrofit.Builder().baseUrl("https://rm.stagingmonster.com/")
                    .addConverterFactory(GsonConverterFactory.create()).build()
                    .create(redmineRequest::class.java)

            val issuesData = redmineRequest.getIssues(username, password)
            withContext(Dispatchers.Main) {
                val any = issuesData.issues[0]
                Log.d("my", any.toString())
            }
        }
    }

}