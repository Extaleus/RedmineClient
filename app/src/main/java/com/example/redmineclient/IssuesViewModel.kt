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
            if(issues.isSuccess){

            }

            withContext(Dispatchers.Main) {
                Log.d("my", issues.toString())
            }
        }
    }
}