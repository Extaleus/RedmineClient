package com.example.redmineclient

import android.util.Log
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
class ProjectsViewModel @Inject constructor(
    private val repository: Repository
) : ViewModel() {
    private val _projectsUiState = MutableStateFlow(ProjectsPageInfo())
    val projectsUiState: StateFlow<ProjectsPageInfo> = _projectsUiState.asStateFlow()

    private val _route = MutableStateFlow(false)
    val route: StateFlow<Boolean> = _route.asStateFlow()

    init {
        getProjects()
    }

    fun invertRoute() {
        _route.update { false }
    }

    private fun getProjects() {
        viewModelScope.launch(Dispatchers.IO) {
            val projects = repository.getProjects()

            if (projects.isSuccess) {
                withContext(Dispatchers.Main) {
                    val projectsSubprojects: MutableMap<Project, MutableList<Project>> =
                        mutableMapOf()

                    projects.getOrNull()?.projects?.forEach { a ->
                        if (a.parent == null) {
                            val list: MutableList<Project> = mutableListOf()
                            projects.getOrNull()?.projects?.forEach { b ->
                                if ((b.parent != null) && (a.id == b.parent.id)) {
                                    list.add(b)
                                }
                            }
                            projectsSubprojects[a] = list
                        }
                    }

                    projectsSubprojects.forEach {
                        Log.d("my", it.toString())
                    }

                    updateUI {
                        ProjectsPageInfo(
                            projects = projects.getOrNull(),
                            false,
                            ""
                        )
                    }
                }
            } else {
                withContext(Dispatchers.Main) {
                    updateUI {
                        ProjectsPageInfo(
                            projects.getOrNull(),
                            false,
                            "Check your username or password"
                        )
                    }
                }
            }
        }
    }

    private fun updateUI(
        update: (ProjectsPageInfo) -> ProjectsPageInfo
    ) {
        _projectsUiState.update { currentState ->
            update.invoke(currentState)
        }
    }
}