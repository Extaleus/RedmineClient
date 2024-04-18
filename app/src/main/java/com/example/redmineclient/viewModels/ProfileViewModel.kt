package com.example.redmineclient.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.redmineclient.ProfileViewState
import com.example.redmineclient.Repository
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
class ProfileViewModel @Inject constructor(
    private val repository: Repository
) : ViewModel() {
    private val _profileUiState = MutableStateFlow(ProfileViewState())
    val profileUiState: StateFlow<ProfileViewState> = _profileUiState.asStateFlow()

    fun setUserId(userId: Int) {
        getProfile(userId)
    }

    private suspend fun updateState(
        update: (ProfileViewState) -> ProfileViewState
    ) {
        withContext(Dispatchers.Main) {
            _profileUiState.update { currentState ->
                update.invoke(currentState)
            }
        }
    }

    private fun getProfile(userId: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            updateState {
                ProfileViewState(
                    isLoading = true,
                )
            }
            val profile = repository.getProfile(userId)
            if (profile.isSuccess) {
                var imageName =
                    "${
                        profile.getOrNull()?.user?.firstname?.lowercase()
                            ?.filterNot { it.isWhitespace() }
                    }${
                        profile.getOrNull()?.user?.lastname?.lowercase()
                            ?.filterNot { it.isWhitespace() }
                    }"
                if (imageName == "") {
                    imageName = "vasiliylitvak"
                }
                updateState {
                    ProfileViewState(
                        profile.getOrNull()?.user,
                        false,
                        image = imageName
                    )
                }
            } else {
                updateState {
                    ProfileViewState(
                        profile.getOrNull()?.user,
                        false,
                        "User not found"
                    )
                }
            }
        }
    }
}
