package com.example.redmineclient.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.redmineclient.App
import com.example.redmineclient.AuthViewState
import com.example.redmineclient.Extensions
import com.example.redmineclient.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import de.palm.composestateevents.consumed
import de.palm.composestateevents.triggered
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
    private val _authUiState = MutableStateFlow(AuthViewState())
    val authUiState: StateFlow<AuthViewState> = _authUiState.asStateFlow()

    init {
        checkAuth()
    }

    fun signIn(login: String, password: String) {
        checkAuth(login, password)
    }

    private suspend fun updateState(
        update: (AuthViewState) -> AuthViewState
    ) {
        withContext(Dispatchers.Main) {
            _authUiState.update { currentState ->
                update.invoke(currentState)
            }
        }
    }

    fun onConsumedAuthSucceededEvent() {
        viewModelScope.launch { updateState { it.copy(authSucceededEvent = consumed()) } }
    }

    private fun checkAuth(login: String? = null, password: String? = null) {
        viewModelScope.launch(Dispatchers.IO) {
            updateState { AuthViewState(isLoading = true) }

            if (login != null && password != null) {
                App.setAuthData(login, password)
            }

            val projects = repository.getProjects()
            if (projects.isSuccess) {
                val encodedJsonStringProjects: String =
                    Extensions.encodeBase64(projects.getOrNull())
                updateState {
                    AuthViewState(
                        isLoading = true,
                        authSucceededEvent = triggered(
                            encodedJsonStringProjects
                        )
                    )
                }
            } else {
                if (login != null && password != null) {
                    updateState {
                        AuthViewState(
                            isLoading = false,
                            message = "Check username and password"
                        )
                    }
                } else {
                    updateState {
                        AuthViewState(
                            isLoading = false,
                            message = "Please log in"
                        )
                    }
                }
            }
        }
    }
}