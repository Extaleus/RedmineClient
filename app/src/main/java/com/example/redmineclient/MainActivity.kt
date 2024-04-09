package com.example.redmineclient

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.redmineclient.ui.theme.RedmineClientTheme

class MainActivity : ComponentActivity() {
    private val loginViewModel = LoginViewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            RedmineClientTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    MainView(loginViewModel)
                }
            }
        }
    }
}

@Composable
fun MainView(loginViewModel: LoginViewModel) {
    Login(loginViewModel)
}

@Composable
fun Login(loginViewModel: LoginViewModel) {
    Box(
        modifier = Modifier
            .fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column (horizontalAlignment = Alignment.End){
            TextField(value = "Login", onValueChange = { /*TODO*/ })
            TextField(value = "Password", onValueChange = { /*TODO*/ })
            Button(onClick = { loginViewModel.loginRequest("maxim.danilichenko", "Jackp0t For A Sucker 1337") }) {
                Text(text = "Sign In")
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun MainViewPreview() {
    RedmineClientTheme {
        MainView(LoginViewModel())
    }
}