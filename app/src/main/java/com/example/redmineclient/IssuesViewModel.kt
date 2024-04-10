package com.example.redmineclient

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class IssuesViewModel @Inject constructor(
    private val issuesRepository: IssuesRepository
) : ViewModel() {

    fun loginRequest() {
        viewModelScope.launch(Dispatchers.IO) {
            val issues = issuesRepository.getIssues()

            withContext(Dispatchers.Main) {
                Log.d("my", issues.toString())
            }
        }
    }

//    private lateinit var redmineRequest: RedmineApi

//    private val interceptor = HttpLoggingInterceptor()
//    private val client: OkHttpClient

//    init {
//        interceptor.level = HttpLoggingInterceptor.Level.BODY
//        client = OkHttpClient.Builder().addInterceptor(interceptor).build()

//        loginRequest()
//    }

//    fun loginRequest() {
//        viewModelScope.launch(Dispatchers.IO) {
//            redmineRequest =
//                Retrofit.Builder().baseUrl("https://rm.stagingmonster.com").client(client)
//                    .addConverterFactory(GsonConverterFactory.create()).build()
//                    .create(RedmineApi::class.java)
//
//            val issuesData = redmineRequest.getIssues()
//            withContext(Dispatchers.Main) {
//                val any = issuesData.issues
//                Log.d("my", any.toString())
//            }
//        }
//    }

}