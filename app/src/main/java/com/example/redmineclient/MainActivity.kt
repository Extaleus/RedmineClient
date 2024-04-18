package com.example.redmineclient

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
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
            Auth(navController)
        }
        composable(
            "projects/{openedProjects}",
            arguments = listOf(navArgument("openedProjects") { type = NavType.StringType })
        ) {
            val jsonStringProjects =
                Extensions.decodeBase64<ProjectsData>(it.arguments?.getString("openedProjects")!!)
            Projects(navController, jsonStringProjects)
        }
        composable(
            "issues/{openedProject}",
            arguments = listOf(navArgument("openedProject") { type = NavType.StringType })
        ) {
            Issues(navController, it.arguments?.getString("openedProject")!!)
        }
        composable(
            "issueinspect/{openedIssue}",
            arguments = listOf(navArgument("openedIssue") { type = NavType.StringType })
        ) {
            val jsonStringIssue =
                Extensions.decodeBase64<Issue>(it.arguments?.getString("openedIssue")!!)

            IssueInspect(navController, jsonStringIssue)
        }
        composable(
            "profile/{openedProfile}",
            arguments = listOf(navArgument("openedProfile") { type = NavType.IntType })
        ) {
            Profile(it.arguments?.getInt("openedProfile")!!)
        }
    }
}

