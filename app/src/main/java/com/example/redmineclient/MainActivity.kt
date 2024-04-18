package com.example.redmineclient

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.redmineclient.components.Auth
import com.example.redmineclient.components.IssueInspect
import com.example.redmineclient.components.Issues
import com.example.redmineclient.components.Profile
import com.example.redmineclient.components.Projects
import com.example.redmineclient.ui.theme.RedmineClientTheme
import com.example.redmineclient.viewModels.AuthViewModel
import com.example.redmineclient.viewModels.IssueInspectViewModel
import com.example.redmineclient.viewModels.IssuesViewModel
import com.example.redmineclient.viewModels.ProfileViewModel
import com.example.redmineclient.viewModels.ProjectsViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            RedmineClientTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background
                ) {
                    MainView()
                }
            }
        }
    }
}

@Composable
fun MainView() {
    val navController = rememberNavController()

    NavHost(navController, startDestination = "Auth") {
        composable("auth") {
            val authViewModel = hiltViewModel<AuthViewModel>()
            Auth(authViewModel, navController)
        }
        composable(
            "projects/{openedProjects}",
            arguments = listOf(navArgument("openedProjects") { type = NavType.StringType })
        ) {
            val projectsViewModel = hiltViewModel<ProjectsViewModel>()
            val jsonStringProjects =
                Extensions.decodeBase64<ProjectsData>(it.arguments?.getString("openedProjects")!!)
            Projects(projectsViewModel, navController, jsonStringProjects)
        }
        composable(
            "issues/{openedProject}",
            arguments = listOf(navArgument("openedProject") { type = NavType.StringType })
        ) {
            val issuesViewModel = hiltViewModel<IssuesViewModel>()
            Issues(issuesViewModel, navController, it.arguments?.getString("openedProject")!!)
        }
        composable(
            "issueinspect/{openedIssue}",
            arguments = listOf(navArgument("openedIssue") { type = NavType.StringType })
        ) {
            val issueInspectViewModel = hiltViewModel<IssueInspectViewModel>()
            val jsonStringIssue =
                Extensions.decodeBase64<Issue>(it.arguments?.getString("openedIssue")!!)
            IssueInspect(issueInspectViewModel, navController, jsonStringIssue)
        }
        composable(
            "profile/{openedProfile}",
            arguments = listOf(navArgument("openedProfile") { type = NavType.IntType })
        ) {
            val profileViewModel = hiltViewModel<ProfileViewModel>()
            Profile(profileViewModel, navController, it.arguments?.getInt("openedProfile")!!)
        }
    }
}

