package com.example.redmineclient.viewModels

import androidx.lifecycle.ViewModel
import androidx.navigation.NavHostController
import com.example.redmineclient.Project
import com.example.redmineclient.ProjectsData
import com.example.redmineclient.ProjectsPageInfo
import com.example.redmineclient.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class ProjectsViewModel @Inject constructor(
    private val repository: Repository
) : ViewModel() {
    private val _projectsUiState = MutableStateFlow(ProjectsPageInfo())
    val projectsUiState: StateFlow<ProjectsPageInfo> = _projectsUiState.asStateFlow()

    private lateinit var navController: NavHostController

//    init {
//        getProjects()
//    }

    // better than init -> getProjects()
    fun setProjectsCold(projectsData: ProjectsData) {
        val mappedProjects = remapProjectsData(projectsData)
        updateUI {
            ProjectsPageInfo(
                mappedProjects,
                false,
                ""
            )
        }
    }

    fun openIssues(project: String) {
        navController.navigate("issues/${project}")
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

    // if needed update project without reload app
//    private fun getProjects() {
//        updateUI { ProjectsPageInfo(null, true, "") }
//        viewModelScope.launch(Dispatchers.IO) {
//            val projects = repository.getProjects()
//
//            if (projects.isSuccess) {
//                val mappedProjects = remapProjectsData(projects.getOrNull()!!)
//
//                withContext(Dispatchers.Main) {
//                    updateUI {
//                        ProjectsPageInfo(
//                            mappedProjects,
//                            false,
//                            ""
//                        )
//                    }
//                }
//            } else {
//                withContext(Dispatchers.Main) {
//                    navController.navigate("auth")
//                    updateUI {
//                        ProjectsPageInfo(
//                            mutableListOf(),
//                            false,
//                            "Check your username or password"
//                        )
//                    }
//                }
//            }
//        }
//    }

    private fun updateUI(
        update: (ProjectsPageInfo) -> ProjectsPageInfo
    ) {
        _projectsUiState.update { currentState ->
            update.invoke(currentState)
        }
    }

    fun putNavController(_navController: NavHostController) {
        navController = _navController
    }
}