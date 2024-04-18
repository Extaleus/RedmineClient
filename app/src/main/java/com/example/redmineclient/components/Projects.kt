package com.example.redmineclient.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.KeyboardArrowDown
import androidx.compose.material.icons.rounded.KeyboardArrowUp
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.example.redmineclient.Project
import com.example.redmineclient.ProjectsData
import com.example.redmineclient.viewModels.ProjectsViewModel

@Composable
fun Projects(
    navController: NavHostController,
    projectsData: ProjectsData
) {
    val projectsViewModel = hiltViewModel<ProjectsViewModel>()
    val projectsUiState by projectsViewModel.projectsUiState.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        projectsViewModel.setProjectsCold(projectsData)
    }

    projectsUiState.message?.let { Text(text = it) }
    Column {
        Row(
            Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center
        ) {
            Text(
                text = "Projects",
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.padding(16.dp)
            )
        }
        LazyColumn {
            projectsUiState.projects?.forEach {
                projectItemView(it, navController)
            }
        }
    }
}

fun LazyListScope.projectItemView(
    project: Project,
    navController: NavHostController
) {
    item {
        var expanded by rememberSaveable { mutableStateOf(false) }
        if (project.subprojects.projects.isNotEmpty()) {
            ElevatedButton(
                onClick = { navController.navigate("issues/${project.name}") },
                shape = RoundedCornerShape(10.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF396B49),
                ),
                border = BorderStroke(1.dp, color = Color(0xFF649D4C)),
                modifier = Modifier
                    .padding(start = 16.dp, end = 16.dp),
            ) {
                Column(
                    Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                    ) {
                        Text(
                            text = project.name,
                            style = MaterialTheme.typography.bodyLarge,
                            modifier = Modifier.align(Alignment.Center)
                        )
                        IconButton(
                            onClick = {
                                expanded = !expanded
                            },
                            Modifier
                                .align(Alignment.CenterEnd)
                                .size(24.dp)
                        ) {
                            Icon(
                                imageVector = if (expanded) Icons.Rounded.KeyboardArrowUp else Icons.Rounded.KeyboardArrowDown,
                                contentDescription = "arrow",
                                tint = Color(0xFF7DF9FD),
                            )

                        }
                    }
                    if (expanded) {
                        project.subprojects.projects.forEach {
                            ElevatedButton(
                                onClick = { navController.navigate("issues/${it.name}") },
                                shape = RoundedCornerShape(5.dp),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = Color(0xFF224F47),
                                ),
                                border = BorderStroke(1.dp, color = Color(0xFF113A45))
                            ) {
                                Text(
                                    it.name, style = MaterialTheme.typography.bodyMedium
                                )
                            }
                        }
                    }
                }
            }
        } else {
            ElevatedButton(
                onClick = { navController.navigate("issues/${project.name}") },
                shape = RoundedCornerShape(10.dp),
                elevation = ButtonDefaults.buttonElevation(
                    defaultElevation = 10.dp, pressedElevation = 8.dp
                ),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF396B49),
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 16.dp, end = 16.dp, bottom = 8.dp),
                border = BorderStroke(1.dp, color = Color(0xFF649D4C))
            ) {
                Text(
                    project.name, style = MaterialTheme.typography.bodyLarge
                )
            }
        }
    }
}
