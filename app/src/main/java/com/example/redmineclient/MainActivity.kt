package com.example.redmineclient

import android.os.Bundle
import android.util.Log
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
import com.example.redmineclient.components.Profile
import com.example.redmineclient.components.Projects
import com.example.redmineclient.ui.theme.RedmineClientTheme
import com.example.redmineclient.viewModels.AuthViewModel
import com.example.redmineclient.viewModels.IssueInspectViewModel
import com.example.redmineclient.viewModels.IssuesViewModel
import com.example.redmineclient.viewModels.ProfileViewModel
import com.example.redmineclient.viewModels.ProjectsViewModel
import com.google.gson.Gson
import dagger.hilt.android.AndroidEntryPoint
import java.net.URLDecoder

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val authViewModel: AuthViewModel by viewModels()
    private val projectsViewModel: ProjectsViewModel by viewModels()
    private val issuesViewModel: IssuesViewModel by viewModels()
    private val issueInspectViewModel: IssueInspectViewModel by viewModels()
    private val profileViewModel: ProfileViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            RedmineClientTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background
                ) {
                    MainView(
                        authViewModel, projectsViewModel, issuesViewModel, issueInspectViewModel, profileViewModel
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
    issueInspectViewModel: IssueInspectViewModel,
    profileViewModel: ProfileViewModel
) {
    val navController = rememberNavController()

    authViewModel.putNavController(navController)
    projectsViewModel.putNavController(navController)
    issuesViewModel.putNavController(navController)
    issueInspectViewModel.putNavController(navController)
    profileViewModel.putNavController(navController)

    NavHost(navController, startDestination = "Auth") {
        composable("auth") {
            Auth(authViewModel)
        }
        composable(
            "projects/{openedProjects}",
            arguments = listOf(navArgument("openedProjects") { type = NavType.StringType })
        ) {
            val encodedJsonStringProjects = it.arguments?.getString("openedProjects")!!
            val decodedJsonStringProjects: String = URLDecoder.decode(encodedJsonStringProjects, "utf-8")
            val jsonStringProjects = Gson().fromJson(decodedJsonStringProjects, ProjectsData::class.java)

            Projects(projectsViewModel, jsonStringProjects)
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
            val encodedJsonStringIssue = it.arguments?.getString("openedIssue")!!
            val decodedJsonStringIssue = URLDecoder.decode(encodedJsonStringIssue, "utf-8")
            val jsonStringIssue = Gson().fromJson(decodedJsonStringIssue, Issue::class.java)

            IssueInspect(issueInspectViewModel, jsonStringIssue)
        }
        composable(
            "profile/{openedProfile}",
            arguments = listOf(navArgument("openedProfile") { type = NavType.IntType })
        ) {
            Log.d("my", "HELLO FROM MAIN ACT: ${it.arguments?.getInt("openedProfile")!!}")
            val userId = it.arguments?.getInt("openedProfile")!!
            Profile(profileViewModel, userId)
        }
    }
}

