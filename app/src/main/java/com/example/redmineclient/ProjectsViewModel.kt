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
                val mappedProjects: MutableList<Project> = mutableListOf()

                projects.getOrNull()?.projects?.forEach {
                    it.subprojects = ProjectsData()
                }

                projects.getOrNull()?.projects?.forEach {
                    if (it.parent == null) {
                        mappedProjects.add(it)
                    }
                }

                projects.getOrNull()?.projects?.forEach {
                    if (it.parent != null) {
                        val index = mappedProjects.indexOfFirst { a -> a.id == it.parent.id }
                        if (index != -1) {
                            mappedProjects[index].subprojects.projects.add(it)
                        }
                    }
                }

//                projects.getOrNull()?.projects?.forEach {
//                    it.subprojects = projects.getOrNull()!!
//                }
//
//                projects.getOrNull()?.projects?.forEach { a ->
//                    var bembe: Project? = null
//                    a.subprojects.projects.forEach { b ->
//                        if(b.name == a.name){
//                            bembe = b
//                        }
//                    }
//                    a.subprojects.projects.remove(bembe)
//                }

//                val projectsSubprojects: MutableMap<Project, MutableList<Project>> =
//                    mutableMapOf()
//
//                projects.getOrNull()!!.projects.forEach { a ->
//                    if (a.parent == null) {
//                        val list: MutableList<Project> = mutableListOf()
//                        projects.getOrNull()!!.projects.forEach { b ->
//                            if ((b.parent != null) && (a.id == b.parent.id)) {
//                                list.add(b)
//                            }
//                        }
//                        projectsSubprojects[a] = list
//                    }
//                }

                mappedProjects.forEach {
                    Log.d("my", it.name)
                    it.subprojects.projects.forEach { a ->
                        Log.d("my", "sub: ${a.name}")
                    }
                }

                withContext(Dispatchers.Main) {
                    updateUI {
                        ProjectsPageInfo(
                            mappedProjects,
                            false,
                            ""
                        )
                    }
                }
            } else {
                withContext(Dispatchers.Main) {
                    updateUI {
                        ProjectsPageInfo(
                            mutableListOf(),
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