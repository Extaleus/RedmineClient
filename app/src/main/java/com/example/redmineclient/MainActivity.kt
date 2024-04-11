package com.example.redmineclient

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material.icons.rounded.KeyboardArrowDown
import androidx.compose.material.icons.rounded.KeyboardArrowUp
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.redmineclient.ui.theme.RedmineClientTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val authViewModel: AuthViewModel by viewModels()
    private val projectsViewModel: ProjectsViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            RedmineClientTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background
                ) {
                    MainView(authViewModel, projectsViewModel)
                }
            }
        }
    }
}

@Composable
fun MainView(authViewModel: AuthViewModel, projectsViewModel: ProjectsViewModel) {
    val navController = rememberNavController()
    val authUiState by authViewModel.authUiState.collectAsStateWithLifecycle()
    val projectsUiState by projectsViewModel.projectsUiState.collectAsStateWithLifecycle()
    val routeAuth by authViewModel.route.collectAsStateWithLifecycle()
    val routeProjects by projectsViewModel.route.collectAsStateWithLifecycle()

    LaunchedEffect(key1 = routeAuth) {
        if (routeAuth) {
            authViewModel.invertRoute()
            navController.navigate("projects")
        }
    }

    LaunchedEffect(key1 = routeProjects) {
        if (routeProjects) {
            projectsViewModel.invertRoute()
//            navController.navigate("auth")
        }
    }

    NavHost(navController, startDestination = "Auth") {
        composable("auth") {
            Auth(authViewModel, authUiState)
        }
        composable("projects") {
            Projects(projectsViewModel, projectsUiState)
        }
    }
}

@Composable
fun Auth(
    authViewModel: AuthViewModel, authUiState: AuthPageInfo
) {
    var loginText by remember { mutableStateOf("") }
    var passwordText by remember { mutableStateOf("") }
    var passwordVisible by rememberSaveable { mutableStateOf(false) }

    Box(
        modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            if (authUiState.isLoading) {
                Text(text = "Loading...")
            } else {
                Text(text = authUiState.message)
                OutlinedTextField(value = loginText,
                    onValueChange = { loginText = it },
                    label = { Text("Login") })
                OutlinedTextField(value = passwordText,
                    onValueChange = { passwordText = it },
                    label = { Text(text = "Password") },
                    modifier = Modifier.padding(top = 10.dp, bottom = 10.dp),
                    singleLine = true,
                    visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                    trailingIcon = {
                        val image = if (passwordVisible) Icons.Filled.Visibility
                        else Icons.Filled.VisibilityOff
                        val description = if (passwordVisible) "Hide password" else "Show password"
                        IconButton(onClick = { passwordVisible = !passwordVisible }) {
                            Icon(imageVector = image, description)
                        }
                    })
                Button(onClick = { authViewModel.signIn(loginText, passwordText) }) {
                    Text(text = "Log in")
                }
            }
        }
    }
}

@Composable
fun Projects(
    projectsViewModel: ProjectsViewModel, projectsUiState: ProjectsPageInfo
) {
    Column(
        modifier = Modifier
            .fillMaxSize(),
//            .background(color = Color.Cyan),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "Projects")
        LazyColumn {
            projectsUiState.projects?.projects?.forEach {
                projectItemView(it)
//                items(it.projects) { project ->
//
//                }
//                    if (project.parent == null) {
//                        it.projects.forEach() { subproject ->
//                            if (subproject.parent?.name == project.name) {
//
//                            }
//                        }
//                        var expanded by rememberSaveable {
//                            mutableStateOf(false)
//                        }
//                        Row(verticalAlignment = Alignment.CenterVertically) {
//                            Text(text = project.name)
////                            IconButton(onClick = {
////                                expanded = !expanded
////                            }) {
////                                Icon(
////                                    modifier = Modifier.size(24.dp),
////                                    imageVector = if (expanded) {
////                                        Icons.Rounded.KeyboardArrowUp
////                                    } else {
////                                        Icons.Rounded.KeyboardArrowDown
////                                    },
////                                    contentDescription = "arrow",
////                                    tint = Color.Black
////                                )
////                            }
//                        }
//                        Text(
//                            modifier = Modifier.padding(start = 20.dp),
//                            text = "${project.name}, ${project.parent}"
//                        )
//                    } else {
//                        Text(text = project.name)
//                    }

            }
        }
    }
}

fun LazyListScope.projectItemView(project: Project) {
    item {
        var expanded by rememberSaveable { mutableStateOf(false) }
        if(project.subprojects.projects.isNotEmpty()){
            Column {
                Text(text = project.name)
                IconButton(onClick = {
                    expanded = !expanded
                }) {
                    Icon(
                        modifier = Modifier,
//                        .size(24.dp),
                        imageVector = if (expanded) Icons.Rounded.KeyboardArrowUp else Icons.Rounded.KeyboardArrowDown,
                        contentDescription = "arrow",
                        tint = Color.White
                    )
                }
            }
        }


    }
}