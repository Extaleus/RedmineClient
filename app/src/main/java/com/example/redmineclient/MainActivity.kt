package com.example.redmineclient

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.redmineclient.ui.theme.RedmineClientTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val issuesViewModel: IssuesViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            RedmineClientTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    MainView(issuesViewModel)
                }
            }
        }
    }
}

@Composable
fun MainView(issuesViewModel: IssuesViewModel) {
    Login(issuesViewModel)
}

@Composable
fun Login(issuesViewModel: IssuesViewModel) {
    var loginText by remember { mutableStateOf("") }
    var passwordText by remember { mutableStateOf("") }

    Box(
        modifier = Modifier
            .fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.End) {
            OutlinedTextField(
                value = loginText,
                onValueChange = { loginText = it },
                label = { Text("Login") })
            OutlinedTextField(
                value = passwordText,
                onValueChange = { passwordText = it },
                label = { Text(text = "Password") },
                modifier = Modifier
                    .padding(top = 10.dp, bottom = 10.dp))

            Button(onClick = {
                issuesViewModel.loginRequest()
            }) {
                Text(text = "Sign In")
            }
        }
    }
}

//@Preview(showBackground = true)
//@Composable
//fun MainViewPreview() {
//    RedmineClientTheme {
//        MainView(IssuesViewModel())
//    }
//}