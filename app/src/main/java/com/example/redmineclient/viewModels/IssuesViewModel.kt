package com.example.redmineclient.viewModels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavHostController
import com.example.redmineclient.Issue
import com.example.redmineclient.IssuesPageInfo
import com.example.redmineclient.Repository
import com.google.gson.Gson
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.net.URLEncoder
import javax.inject.Inject

@HiltViewModel
class IssuesViewModel @Inject constructor(
    private val repository: Repository
) : ViewModel() {
    private val _issuesUiState = MutableStateFlow(IssuesPageInfo())
    val issuesUiState: StateFlow<IssuesPageInfo> = _issuesUiState.asStateFlow()

    private lateinit var navController: NavHostController

    private var projectName: String? = null

    fun setProjectName(_projectName: String) {
        projectName = _projectName
        getIssues()
    }

    private lateinit var issuesList: MutableList<Issue>

    fun onClickIssue(issue: Issue) {
        val jsonStringIssue = Gson().toJson(issue)
        val encodedJsonStringIssue = URLEncoder.encode(jsonStringIssue, "utf-8")

        navController.navigate("issueinspect/${encodedJsonStringIssue}")
    }

    fun onClickProfile(issue: Issue) {
        val userId: Int = issue.assigned_to.id
        Log.d("my", "IVM USERID: $userId")
        navController.navigate("profile/${userId}")
    }

    private fun getIssues() {
        updateUI {
            IssuesPageInfo(
                null,
                true,
            )
        }
        viewModelScope.launch(Dispatchers.IO) {
            val issues = repository.getIssues()

            if (issues.isSuccess) {
                val mappedIssues: MutableList<Issue> = mutableListOf()

                issues.getOrNull()?.issues?.forEach {
                    if (it.project.name == projectName) {
                        mappedIssues.add(it)
                    }
                }

                withContext(Dispatchers.Main) {
                    if (mappedIssues.isEmpty()) {
                        updateUI {
                            IssuesPageInfo(
                                mappedIssues,
                                false,
                                "Issues is empty"
                            )
                        }
                    } else {
                        issuesList = mappedIssues
                        updateUI {
                            IssuesPageInfo(
                                mappedIssues,
                                false,
                            )
                        }
                    }
                }
            } else {
                getIssues()
                withContext(Dispatchers.Main) {
                    updateUI {
                        navController.navigate("auth")
                        IssuesPageInfo(
                            mutableListOf(),
                            false,
                        )
                    }
                }
            }
        }
    }

    private fun updateUI(
        update: (IssuesPageInfo) -> IssuesPageInfo
    ) {
        _issuesUiState.update { currentState ->
            update.invoke(currentState)
        }
    }

    fun putNavController(_navController: NavHostController) {
        navController = _navController
    }
}