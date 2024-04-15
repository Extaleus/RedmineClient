package com.example.redmineclient.viewModels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavHostController
import com.example.redmineclient.IssueInfo
import com.example.redmineclient.IssuesPageInfo
import com.example.redmineclient.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class IssueInspectViewModel @Inject constructor(
    private val repository: Repository
) : ViewModel() {
    private val _issueInspectUiState = MutableStateFlow(IssueInfo())
    val issueInspectUiState: StateFlow<IssueInfo> = _issueInspectUiState.asStateFlow()

    private lateinit var navController: NavHostController

    fun setIssueId(issueId: Int) {
        getIssueAttachments(issueId)
    }

    private fun getIssueAttachments(issueId: Int) {
        updateUI {
            IssueInfo(
                null,
                true
            )
        }
        viewModelScope.launch(Dispatchers.IO) {
            val issue = repository.getIssueAttachments(issueId)
            Log.d("my", issue.getOrNull().toString())

            if (issue.isSuccess) {
                withContext(Dispatchers.Main) {
                    updateUI {
                        IssueInfo(
                            issue.getOrNull()?.issue,
                            false
                        )
                    }
                }
            } else {
                withContext(Dispatchers.Main) {
                    navController.navigate("auth")
                    updateUI {
                        IssueInfo(
                            null,
                            false
                        )
                    }
                }
            }
        }
    }

    private fun updateUI(
        update: (IssueInfo) -> IssueInfo
    ) {
        _issueInspectUiState.update { currentState ->
            update.invoke(currentState)
        }
    }

    fun putNavController(_navController: NavHostController) {
        navController = _navController
    }
}