package com.example.redmineclient.viewModels

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavHostController
import com.example.redmineclient.AndroidDownloader
import com.example.redmineclient.Issue
import com.example.redmineclient.IssueInfo
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

    private lateinit var downloader: AndroidDownloader

    val issueStatus: List<String> =
        listOf(
            "",
            "New",
            "Assigned",
            "Resolved",
            "Feedback",
            "Closed",
            "Rejected",
            "Started",
            "In progress",
            "Ready for review",
            "Awaiting client feedback",
            "In queue",
            "In Testing",
            "Completed"
        )

    fun downloadFile(context: Context, url: String, contentType: String, fileName: String) {
        Log.d("my", "Downlaod fIle")
        downloader = AndroidDownloader(context)
        downloader.downloadFile(url, contentType, fileName)
    }

    fun onClickProfile(issue: Issue) {
        val userId: Int? = issue.assigned_to?.id
        Log.d("my", "IVM USERID: $userId")
        navController.navigate("profile/${userId}")
    }

    fun setIssueId(issueId: Int) {
        getIssueAttachments(issueId)
    }

    fun setUserId(issue: Issue, userId: Int) {
        getUserById(issue, userId)
    }

    private fun getUserById(issue: Issue, userId: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            val profile = repository.getProfile(userId)
            if (profile.isSuccess) {
                val nameById =
                    "${profile.getOrNull()?.user?.firstname} ${profile.getOrNull()?.user?.lastname}"
                withContext(Dispatchers.Main) {
                    updateUI {
                        IssueInfo(
                            issue,
                            false,
                            nameById
                        )
                    }
                }
            } else {
                withContext(Dispatchers.Main) {
                    updateUI {
                        IssueInfo(
                            issue,
                            false,
                            "User not found"
                        )
                    }
                }
            }
        }
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
                getIssueAttachments(issueId)
                withContext(Dispatchers.Main) {
//                    navController.navigate("auth")
                    updateUI {
                        IssueInfo(
                            null,
                            false,
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