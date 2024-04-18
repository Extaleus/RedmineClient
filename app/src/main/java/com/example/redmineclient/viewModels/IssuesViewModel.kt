package com.example.redmineclient.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.redmineclient.Extensions
import com.example.redmineclient.Issue
import com.example.redmineclient.IssuesViewState
import com.example.redmineclient.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import de.palm.composestateevents.consumed
import de.palm.composestateevents.triggered
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class IssuesViewModel @Inject constructor(
    private val repository: Repository
) : ViewModel() {
    private val _issuesUiState = MutableStateFlow(IssuesViewState())
    val issuesUiState: StateFlow<IssuesViewState> = _issuesUiState.asStateFlow()

    fun setProjectName(projectName: String) {
        getIssues(projectName)
    }

    fun onClickIssue(issue: Issue) {
        val encodedJsonStringIssue = Extensions.encodeBase64(issue)

        viewModelScope.launch {
            updateState {
                IssuesViewState(
                    issuesSucceededEvent = triggered(
                        encodedJsonStringIssue
                    )
                )
            }
        }
    }

    fun onConsumedIssuesSucceededEvent() {
        viewModelScope.launch { updateState { it.copy(issuesSucceededEvent = consumed()) } }
    }

    private suspend fun updateState(
        update: (IssuesViewState) -> IssuesViewState
    ) {
        withContext(Dispatchers.Main) {
            _issuesUiState.update { currentState ->
                update.invoke(currentState)
            }
        }
    }

    private fun getIssues(projectName: String) {
        viewModelScope.launch(Dispatchers.IO) {
            updateState {
                IssuesViewState(
                    isLoading = true,
                )
            }
            val issues = repository.getIssues()
            if (issues.isSuccess) {
                val mappedIssues: MutableList<Issue> = mutableListOf()
                issues.getOrNull()?.issues?.forEach {
                    if (it.project.name == projectName) {
                        mappedIssues.add(it)
                    }
                }
                if (mappedIssues.isNotEmpty()) {
                    updateState {
                        IssuesViewState(
                            issues = mappedIssues,
                            isLoading = false,
                        )
                    }
                } else {
                    updateState {
                        IssuesViewState(
                            mappedIssues,
                            isLoading = false,
                            message = "Issues is empty"
                        )
                    }
                }
            } else {
                updateState {
                    IssuesViewState(
                        isLoading = false,
                        message = "Issues not found"
                    )
                }
            }
        }
    }
}