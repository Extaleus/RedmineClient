package com.example.redmineclient.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.KeyboardArrowDown
import androidx.compose.material.icons.rounded.KeyboardArrowUp
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.redmineclient.Project
import com.example.redmineclient.ProjectsPageInfo
import com.example.redmineclient.ProjectsViewModel

@Composable
fun Projects(
    projectsViewModel: ProjectsViewModel, projectsUiState: ProjectsPageInfo
) {
    Column(
        modifier = Modifier
            .fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "Projects", modifier = Modifier.padding(bottom = 20.dp))
        LazyColumn {
            projectsUiState.projects?.forEach {
                projectItemView(it, projectsViewModel)
            }
        }
    }
}

fun LazyListScope.projectItemView(project: Project, projectsViewModel: ProjectsViewModel) {
    item {
        var expanded by rememberSaveable { mutableStateOf(false) }
        if (project.subprojects.projects.isNotEmpty()) {
            Column {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    TextButton(onClick = { projectsViewModel.openIssues(project.name) }) {
                        Text(
                            project.name,
                            fontSize = 24.sp,
                        )
                    }
                    IconButton(onClick = {
                        expanded = !expanded
                    }) {
                        Icon(
                            modifier = Modifier
                                .size(24.dp),
                            imageVector = if (expanded) Icons.Rounded.KeyboardArrowUp else Icons.Rounded.KeyboardArrowDown,
                            contentDescription = "arrow",
                            tint = Color.White
                        )
                    }
                }
                if (expanded) {
                    Column {
                        project.subprojects.projects.forEach {
                            TextButton(onClick = { projectsViewModel.openIssues(it.name) }) {
                                Text(
                                    it.name,
                                    fontSize = 18.sp,
                                )
                            }
                        }
                    }
                }
            }
        } else {
            TextButton(onClick = { projectsViewModel.openIssues(project.name) }) {
                Text(
                    project.name,
                    fontSize = 24.sp,
                )
            }
        }
    }
}