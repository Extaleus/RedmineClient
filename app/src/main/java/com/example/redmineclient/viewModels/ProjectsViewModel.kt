package com.example.redmineclient.viewModels

import androidx.lifecycle.ViewModel
import com.example.redmineclient.App
import com.example.redmineclient.Project
import com.example.redmineclient.ProjectsData
import com.example.redmineclient.ProjectsViewState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class ProjectsViewModel @Inject constructor() : ViewModel() {
    private val _projectsUiState = MutableStateFlow(ProjectsViewState())
    val projectsUiState: StateFlow<ProjectsViewState> = _projectsUiState.asStateFlow()

    private fun updateState(
        update: (ProjectsViewState) -> ProjectsViewState
    ) {
        _projectsUiState.update { currentState ->
            update.invoke(currentState)
        }
    }

    fun signOut() {
        App.setAuthData("", "")
    }

    fun setProjectsCold(projectsData: ProjectsData) {
        val mappedProjects = remapProjectsData(projectsData)
        updateState {
            ProjectsViewState(
                projects = mappedProjects
            )
        }
    }

    private fun remapProjectsData(projects: ProjectsData): MutableList<Project> {
        val mappedProjects: MutableList<Project> = mutableListOf()

        projects.projects.forEach {
            if (it.parent == null) {
                it.subprojects = ProjectsData()
                mappedProjects.add(it)
            }
        }

        projects.projects.forEach {
            if (it.parent != null) {
                val index = mappedProjects.indexOfFirst { a -> a.id == it.parent.id }
                if (index != -1) {
                    mappedProjects[index].subprojects.projects.add(it)
                }
            }
        }

        return mappedProjects
    }
}