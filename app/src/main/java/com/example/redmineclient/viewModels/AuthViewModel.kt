package com.example.redmineclient.viewModels

import android.os.Build
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavHostController
import com.example.redmineclient.App
import com.example.redmineclient.AuthPageInfo
import com.example.redmineclient.Repository
import com.google.gson.Gson
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.Base64
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val repository: Repository
) : ViewModel() {
    private val _authUiState = MutableStateFlow(AuthPageInfo())
    val authUiState: StateFlow<AuthPageInfo> = _authUiState.asStateFlow()

    private lateinit var navController: NavHostController

    init {
        checkAuth()
    }

    private fun checkAuth(){
        updateUI { AuthPageInfo(true, "") }
        viewModelScope.launch(Dispatchers.IO) {
            val projects = repository.getProjects()
            if (projects.isSuccess) {
                withContext(Dispatchers.Main) {
                    updateUI {
                        AuthPageInfo(
                            false,
                            ""
                        )
                    }

                    val jsonStringProjects = Gson().toJson(projects.getOrNull())

                    val encodedJsonStringProjects: String = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        Base64.getEncoder().encodeToString(jsonStringProjects.toByteArray())
                    } else {
                        TODO("VERSION.SDK_INT < O")
                    }

                    navController.navigate("projects/${encodedJsonStringProjects}"){
                        popUpTo(0) {
                            inclusive = true
                        }
                    }
                }
            }else{
                withContext(Dispatchers.Main) {
                    updateUI {
                        AuthPageInfo(
                            false,
                            ""
                        )
                    }
                }
            }
        }
    }

    fun signIn(login: String, password: String){
        updateUI {
            AuthPageInfo(
                isLoading = true,
                message = ""
            )
        }
        App.setAuthData(login, password)
        loginRequest()
    }

//    fun signOut(){
//        updateUI {
//            AuthPageInfo(
//                message = ""
//            )
//        }
//        App.setAuthData("", "")
//    }

    private fun loginRequest() {
        viewModelScope.launch(Dispatchers.IO) {
            val projects = repository.getProjects()
            if (projects.isSuccess) {
                withContext(Dispatchers.Main) {
                    updateUI {
                        AuthPageInfo(
                            false,
                            ""
                        )
                    }
                    val jsonStringProjects = Gson().toJson(projects.getOrNull())

                    val encodedJsonStringProjects: String = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        Base64.getEncoder().encodeToString(jsonStringProjects.toByteArray())
                    } else {
                        TODO("VERSION.SDK_INT < O")
                    }

                    navController.navigate("projects/${encodedJsonStringProjects}"){
                        popUpTo(0) {
                            inclusive = true
                        }
                    }
                }
            }else{
                withContext(Dispatchers.Main) {
                    updateUI {
                        AuthPageInfo(
                            false,
                            "Check your username or password"
                        )
                    }
                }
            }
        }
    }

    private fun updateUI(
        update: (AuthPageInfo) -> AuthPageInfo
    ) {
        _authUiState.update { currentState ->
            update.invoke(currentState)
        }
    }

    fun putNavController(_navController: NavHostController) {
        navController = _navController
    }
}