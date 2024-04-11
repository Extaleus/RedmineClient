package com.example.redmineclient

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.redmineclient.App.Companion.getAuthData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val repository: Repository
) : ViewModel() {
    private val _authUiState = MutableStateFlow(AuthPageInfo())
    val authUiState: StateFlow<AuthPageInfo> = _authUiState.asStateFlow()

    private val _route = MutableStateFlow(false)
    val route: StateFlow<Boolean> = _route.asStateFlow()

    init {
        checkAuth()
    }

    fun invertRoute(){
        _route.update { false }
    }

    private fun checkAuth(){
        updateUI { AuthPageInfo(true, "") }
        viewModelScope.launch(Dispatchers.IO) {
            val projects = repository.getProjects()
            if (projects.isSuccess) {
                withContext(Dispatchers.Main) {
                    _route.update { true }
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
//                    updateUI {
//                        AuthPageInfo(
//                            false,
//                            "Welcome"
//                        )
//                    }
                    _route.update { true }
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
}