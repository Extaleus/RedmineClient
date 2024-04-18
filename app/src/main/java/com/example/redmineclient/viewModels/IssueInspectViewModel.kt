package com.example.redmineclient.viewModels

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.redmineclient.AndroidDownloader
import com.example.redmineclient.Issue
import com.example.redmineclient.IssueViewState
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
    private val _issueInspectUiState = MutableStateFlow(IssueViewState())
    val issueInspectUiState: StateFlow<IssueViewState> = _issueInspectUiState.asStateFlow()

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
        downloader = AndroidDownloader(context)
        downloader.downloadFile(url, contentType, fileName)
    }

    fun setIssueId(issueId: Int) {
        getIssueAttachments(issueId)
    }

    fun setUserId(issue: Issue, userId: Int) {
        getUserById(issue, userId)
    }

    private suspend fun updateState(
        update: (IssueViewState) -> IssueViewState
    ) {
        withContext(Dispatchers.Main) {
            _issueInspectUiState.update { currentState ->
                update.invoke(currentState)
            }
        }
    }

    private fun getUserById(issue: Issue, userId: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            val profile = repository.getProfile(userId)
            if (profile.isSuccess) {
                val nameById =
                    "${profile.getOrNull()?.user?.firstname} ${profile.getOrNull()?.user?.lastname}"
                updateState {
                    IssueViewState(
                        issue = issue,
                        isLoading = false,
                        nameById = nameById
                    )
                }
            } else {
                updateState {
                    IssueViewState(
                        issue = issue,
                        isLoading = false,
                        message = "User not found"
                    )
                }

            }
        }
    }

    private fun getIssueAttachments(issueId: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            updateState {
                IssueViewState(
                    isLoading = true
                )
            }
            val issue = repository.getIssueAttachments(issueId)
            if (issue.isSuccess) {
                updateState {
                    IssueViewState(
                        issue = issue.getOrNull()?.issue,
                        isLoading = false
                    )
                }
            } else {
                updateState {
                    IssueViewState(
                        isLoading = false,
                        message = "Attachments not found"
                    )
                }
            }
        }
    }
}
