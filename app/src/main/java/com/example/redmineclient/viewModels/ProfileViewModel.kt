package com.example.redmineclient.viewModels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavHostController
import com.example.redmineclient.ProfilePageInfo
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
    private val _profileUiState = MutableStateFlow(ProfilePageInfo())
    val profileUiState: StateFlow<ProfilePageInfo> = _profileUiState.asStateFlow()

    private lateinit var navController: NavHostController

    fun setUserId(userId: Int) {
        getProfile(userId)
    }

    private fun getProfile(userId: Int) {
//        updateUI {
//            ProfilePageInfo(
//                null,
//                true,
//            )
//        }
        viewModelScope.launch(Dispatchers.IO) {
            val profile = repository.getProfile(userId)
            Log.d("my", "PVM PROF: " + profile.getOrNull().toString())
            if (profile.isSuccess) {
                val imageName =
                    "${profile.getOrNull()?.user?.firstname?.lowercase()}${profile.getOrNull()?.user?.lastname?.lowercase()}"
                withContext(Dispatchers.Main) {
                    updateUI {
                        ProfilePageInfo(
                            profile.getOrNull()?.user,
                            false,
                            image = imageName
                        )
                    }
                }
            } else {
//                getProfile(userId)
                withContext(Dispatchers.Main) {
                    updateUI {
                        ProfilePageInfo(
                            profile.getOrNull()?.user,
                            false,
                            "User not found"
                        )
                    }
                }
            }
        }
    }

    private fun updateUI(
        update: (ProfilePageInfo) -> ProfilePageInfo
    ) {
        _profileUiState.update { currentState ->
            update.invoke(currentState)
        }
    }

    fun putNavController(_navController: NavHostController) {
        navController = _navController
    }
}