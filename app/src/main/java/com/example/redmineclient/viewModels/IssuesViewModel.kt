package com.example.redmineclient.viewModels

import android.os.Build
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
import java.util.Base64
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

    fun onClickIssue(_issue: Issue){
        val jsonStringIssue = Gson().toJson(_issue)

        val encodedJsonStringIssue: String = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            Base64.getEncoder().encodeToString(jsonStringIssue.toByteArray())
        } else {
            TODO("VERSION.SDK_INT < O")
        }

        navController.navigate("issueinspect/${encodedJsonStringIssue}")
    }

    private fun getIssues() {
        IssuesPageInfo(
            null,
            true,
        )
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
                    if (mappedIssues.isEmpty()){
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