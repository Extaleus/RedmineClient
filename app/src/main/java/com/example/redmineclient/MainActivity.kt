package com.example.redmineclient

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.redmineclient.components.Auth
import com.example.redmineclient.components.IssueInspect
import com.example.redmineclient.components.Issues
import com.example.redmineclient.components.Projects
import com.example.redmineclient.ui.theme.RedmineClientTheme
import com.example.redmineclient.viewModels.AuthViewModel
import com.example.redmineclient.viewModels.IssueInspectViewModel
import com.example.redmineclient.viewModels.IssuesViewModel
import com.example.redmineclient.viewModels.ProjectsViewModel
import com.google.gson.Gson
import com.squareup.moshi.Moshi
import dagger.hilt.android.AndroidEntryPoint
import java.util.Base64

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val authViewModel: AuthViewModel by viewModels()
    private val projectsViewModel: ProjectsViewModel by viewModels()
    private val issuesViewModel: IssuesViewModel by viewModels()
    private val issueInspectViewModel: IssueInspectViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            RedmineClientTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background
                ) {
                    MainView(
                        authViewModel,
                        projectsViewModel,
                        issuesViewModel,
                        issueInspectViewModel
                    )
                }
            }
        }
    }
}

@Composable
fun MainView(
    authViewModel: AuthViewModel,
    projectsViewModel: ProjectsViewModel,
    issuesViewModel: IssuesViewModel,
    issueInspectViewModel: IssueInspectViewModel
) {
    val navController = rememberNavController()

    authViewModel.putNavController(navController)
    projectsViewModel.putNavController(navController)
    issuesViewModel.putNavController(navController)
    issueInspectViewModel.putNavController(navController)

    NavHost(navController, startDestination = "Auth") {
        composable("auth") {
            Auth(authViewModel)
        }
        composable("projects") {
            Projects(projectsViewModel)
        }
        composable(
            "issues/{openedProject}",
            arguments = listOf(navArgument("openedProject") { type = NavType.StringType })
        ) {
            Issues(issuesViewModel, it.arguments?.getString("openedProject")!!)
        }
        composable(
            "issueinspect/{openedIssue}",
            arguments = listOf(navArgument("openedIssue") { type = NavType.StringType })
        ) {
            val encodedJsonStringIssue = it.arguments?.getString("openedIssue")

            val decodedJsonStringIssue: String = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                String(Base64.getDecoder().decode(encodedJsonStringIssue))
            } else {
                TODO("VERSION.SDK_INT < O")
            }

            val jsonStringIssue = Gson().fromJson(decodedJsonStringIssue, Issue::class.java)

            IssueInspect(issueInspectViewModel, jsonStringIssue)
        }
    }
}

