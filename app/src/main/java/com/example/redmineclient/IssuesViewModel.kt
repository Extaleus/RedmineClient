package com.example.redmineclient

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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
class IssuesViewModel @Inject constructor(
    private val repository: Repository
) : ViewModel() {
    private val _issuesUiState = MutableStateFlow(IssuesPageInfo())
    val issuesUiState: StateFlow<IssuesPageInfo> = _issuesUiState.asStateFlow()

    private val _route = MutableStateFlow(false)
    val route: StateFlow<Boolean> = _route.asStateFlow()

    private var projectName: String? = null

//    fun invertRoute() {
//        _route.update { false }
//    }
//
//    fun openIssues() {
//        _route.update { true }
//    }

    fun setProjectName(_projectName: String) {
        projectName = _projectName
        IssuesPageInfo(
            mutableListOf(),
            true,
//                            "Check your username or password"
        )
        getIssues()
    }

    private fun getIssues() {
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
                    updateUI {
                        IssuesPageInfo(
                            mappedIssues,
                            false,
//                            ""
                        )
                    }
                }
            } else {
                withContext(Dispatchers.Main) {
                    updateUI {
                        IssuesPageInfo(
                            mutableListOf(),
                            false,
//                            "Check your username or password"
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
}