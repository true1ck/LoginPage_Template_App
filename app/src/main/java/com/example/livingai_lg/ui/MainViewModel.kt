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

    /**
     * Public method to refresh auth status after login/signup
     * Call this after tokens are saved to update the auth state
     */
    fun refreshAuthStatus() {
        checkAuthStatus()
    }

    private fun checkAuthStatus() {
        viewModelScope.launch {
            val accessToken = tokenManager.getAccessToken()
            val refreshToken = tokenManager.getRefreshToken()
            
            if (accessToken != null && refreshToken != null) {
                // Tokens exist, validate them by fetching user details
                // The Ktor Auth plugin will automatically refresh if access token is expired
                validateTokens()
            } else {
                // No tokens, user is not authenticated
                _authState.value = AuthState.Unauthenticated
            }
        }
    }

    private fun validateTokens() {
        viewModelScope.launch {
            // Try to fetch user details - this will validate the access token
            // If access token is expired, Ktor's Auth plugin will auto-refresh
            authApiClient.getUserDetails()
                .onSuccess { userDetails ->
                    // Tokens are valid, user is authenticated
                    _authState.value = AuthState.Authenticated
                    _userState.value = UserState.Success(userDetails)
                }
                .onFailure { error ->
                    // If fetching user details failed, try manual refresh
                    Log.d(TAG, "Failed to fetch user details, attempting token refresh: ${error.message}")
                    attemptTokenRefresh()
                }
        }
    }

    private fun attemptTokenRefresh() {
        viewModelScope.launch {
            authApiClient.refreshToken()
                .onSuccess { refreshResponse ->
                    // Refresh successful, tokens are valid
                    Log.d(TAG, "Token refresh successful")
                    _authState.value = AuthState.Authenticated
                    // Fetch user details with new token
                    fetchUserDetails()
                }
                .onFailure { error ->
                    // Refresh failed, tokens are invalid - clear and logout
                    Log.d(TAG, "Token refresh failed: ${error.message}")
                    tokenManager.clearTokens()
                    _authState.value = AuthState.Unauthenticated
                    _userState.value = UserState.Error("Session expired. Please sign in again.")
                }
        }
    }

    fun fetchUserDetails() {
        viewModelScope.launch {
            _userState.value = UserState.Loading
            authApiClient.getUserDetails()
                .onSuccess {
                    _userState.value = UserState.Success(it)
                    _authState.value = AuthState.Authenticated
                }
                .onFailure {
                    _userState.value = UserState.Error(it.message ?: "Unknown error")
                    // Don't automatically set to Unauthenticated here - let the caller decide
                    // or try refresh if needed
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
