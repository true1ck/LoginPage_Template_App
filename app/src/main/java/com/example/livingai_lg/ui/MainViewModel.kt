package com.example.livingai_lg.ui

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.livingai_lg.api.AuthApiClient
import com.example.livingai_lg.api.TokenManager
import com.example.livingai_lg.api.UserDetails
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

private const val TAG = "MainViewModel"

sealed class AuthState {
    object Unknown : AuthState()
    object Authenticated : AuthState()
    object Unauthenticated : AuthState()
}

sealed class UserState {
    object Loading : UserState()
    data class Success(val userDetails: UserDetails) : UserState()
    data class Error(val message: String) : UserState()
}

class MainViewModel(context: Context) : ViewModel() {

    private val tokenManager = TokenManager(context)
    private val authApiClient = AuthApiClient(context)

    private val _authState = MutableStateFlow<AuthState>(AuthState.Unknown)
    val authState = _authState.asStateFlow()

    private val _userState = MutableStateFlow<UserState>(UserState.Loading)
    val userState = _userState.asStateFlow()

    init {
        checkAuthStatus()
    }

    private fun checkAuthStatus() {
        viewModelScope.launch {
            if (tokenManager.getAccessToken() != null) {
                _authState.value = AuthState.Authenticated
                fetchUserDetails()
            } else {
                _authState.value = AuthState.Unauthenticated
            }
        }
    }

    fun fetchUserDetails() {
        viewModelScope.launch {
            _userState.value = UserState.Loading
            authApiClient.getUserDetails()
                .onSuccess {
                    _userState.value = UserState.Success(it)
                }
                .onFailure {
                    _userState.value = UserState.Error(it.message ?: "Unknown error")
                    _authState.value = AuthState.Unauthenticated
                }
        }
    }

    fun logout() {
        viewModelScope.launch {
            Log.d(TAG, "Logout process started")
            authApiClient.logout()
                .onSuccess {
                    Log.d(TAG, "Logout successful")
                    _authState.value = AuthState.Unauthenticated
                }
                .onFailure {
                    Log.e(TAG, "Logout failed", it)
                    // Even if the API call fails, force the user to an unauthenticated state
                    _authState.value = AuthState.Unauthenticated
                }
        }
    }
}
