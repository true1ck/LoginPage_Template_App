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
        // Immediately check if tokens exist (synchronous check)
        val accessToken = tokenManager.getAccessToken()
        val refreshToken = tokenManager.getRefreshToken()
        val hasTokens = accessToken != null && refreshToken != null
        
        Log.d(TAG, "MainViewModel.init: accessToken=${accessToken != null}, refreshToken=${refreshToken != null}, hasTokens=$hasTokens")
        
        if (hasTokens) {
            // Tokens exist - optimistically set to Authenticated for immediate navigation
            // Then validate in background (this prevents redirect to landing page on app restart)
            Log.d(TAG, "MainViewModel.init: Tokens found, setting authState to Authenticated (optimistic)")
            _authState.value = AuthState.Authenticated
            // Validate tokens in background (this will only revert if there's a clear auth failure)
            checkAuthStatus()
        } else {
            // No tokens, immediately set to unauthenticated
            Log.d(TAG, "MainViewModel.init: No tokens found, setting authState to Unauthenticated")
            _authState.value = AuthState.Unauthenticated
        }
    }

    /**
     * Public method to refresh auth status after login/signup
     * Call this after tokens are saved to update the auth state
     * 
     * This method optimistically sets authState to Authenticated if tokens exist,
     * then validates in the background. This ensures immediate navigation.
     * 
     * IMPORTANT: After optimistic authentication, we only revert to Unauthenticated
     * if there's a clear authentication failure (not network errors).
     */
    fun refreshAuthStatus() {
        viewModelScope.launch {
            val accessToken = tokenManager.getAccessToken()
            val refreshToken = tokenManager.getRefreshToken()
            
            Log.d(TAG, "refreshAuthStatus: accessToken=${accessToken != null}, refreshToken=${refreshToken != null}")
            
            if (accessToken != null && refreshToken != null) {
                // Optimistically set to Authenticated for immediate navigation
                // Then validate in background
                Log.d(TAG, "Setting authState to Authenticated (optimistic)")
                _authState.value = AuthState.Authenticated
                // Validate tokens in background (this will only revert if there's a clear auth failure)
                validateTokensOptimistic()
            } else {
                Log.d(TAG, "No tokens found, setting authState to Unauthenticated")
                _authState.value = AuthState.Unauthenticated
            }
        }
    }
    
    /**
     * Validates tokens after optimistic authentication.
     * Only reverts authState if there's a clear authentication failure (not network errors).
     * This prevents users from being logged out due to temporary network issues.
     */
    private fun validateTokensOptimistic() {
        viewModelScope.launch {
            Log.d(TAG, "validateTokensOptimistic: Starting token validation")
            // Try to fetch user details first - Ktor's Auth plugin will auto-refresh if access token is expired
            authApiClient.getUserDetails()
                .onSuccess { userDetails ->
                    // Tokens are valid, user is authenticated
                    Log.d(TAG, "validateTokensOptimistic: Token validation successful - user authenticated, userId=${userDetails.id}")
                    _authState.value = AuthState.Authenticated
                    _userState.value = UserState.Success(userDetails)
                }
                .onFailure { error ->
                    Log.w(TAG, "validateTokensOptimistic: getUserDetails failed - ${error.message}")
                    // Check if this is a network error or authentication error
                    val isNetworkError = error.message?.contains("Unable to resolve host", ignoreCase = true) == true
                            || error.message?.contains("timeout", ignoreCase = true) == true
                            || error.message?.contains("network", ignoreCase = true) == true
                            || error.message?.contains("connection", ignoreCase = true) == true
                            || error.message?.contains("SocketTimeoutException", ignoreCase = true) == true
                            || error.message?.contains("ConnectException", ignoreCase = true) == true
                            || error.message?.contains("UnknownHostException", ignoreCase = true) == true
                    
                    Log.d(TAG, "validateTokensOptimistic: isNetworkError=$isNetworkError")
                    
                    if (isNetworkError) {
                        // Network error - keep optimistic authentication state
                        // User might be offline, tokens are still valid
                        Log.w(TAG, "validateTokensOptimistic: Network error detected (keeping optimistic auth): ${error.message}")
                        _userState.value = UserState.Error("Network error. Please check your connection.")
                        // Keep authState as Authenticated - don't revert on network errors
                        Log.d(TAG, "validateTokensOptimistic: Keeping authState as Authenticated despite network error")
                        return@launch
                    }
                    
                    // Check if it's a clear authentication error (401, 403, or specific auth error messages)
                    val isAuthError = error.message?.contains("401", ignoreCase = true) == true
                            || error.message?.contains("403", ignoreCase = true) == true
                            || error.message?.contains("Unauthorized", ignoreCase = true) == true
                            || error.message?.contains("Forbidden", ignoreCase = true) == true
                            || error.message?.contains("Invalid token", ignoreCase = true) == true
                            || error.message?.contains("expired token", ignoreCase = true) == true
                            || error.message?.contains("Invalid refresh token", ignoreCase = true) == true
                    
                    if (isAuthError) {
                        // Clear authentication error - try refresh as last resort
                        Log.d(TAG, "Authentication error detected, attempting token refresh: ${error.message}")
                        
                        authApiClient.refreshToken()
                            .onSuccess { refreshResponse ->
                                // Refresh successful, now try fetching user details again
                                Log.d(TAG, "Token refresh successful, fetching user details")
                                authApiClient.getUserDetails()
                                    .onSuccess { userDetails ->
                                        _authState.value = AuthState.Authenticated
                                        _userState.value = UserState.Success(userDetails)
                                    }
                                    .onFailure { fetchError ->
                                        // Even after refresh, fetching failed - check if network error
                                        val isFetchNetworkError = fetchError.message?.contains("Unable to resolve host", ignoreCase = true) == true
                                                || fetchError.message?.contains("timeout", ignoreCase = true) == true
                                                || error.message?.contains("network", ignoreCase = true) == true
                                        
                                        if (isFetchNetworkError) {
                                            // Network error - keep optimistic auth
                                            Log.w(TAG, "Network error after refresh (keeping optimistic auth): ${fetchError.message}")
                                            _userState.value = UserState.Error("Network error. Please check your connection.")
                                            // Keep authState as Authenticated
                                        } else {
                                            // Clear auth failure - revert to unauthenticated
                                            Log.e(TAG, "Authentication failed after refresh: ${fetchError.message}")
                                            tokenManager.clearTokens()
                                            _authState.value = AuthState.Unauthenticated
                                            _userState.value = UserState.Error("Session expired. Please sign in again.")
                                        }
                                    }
                            }
                            .onFailure { refreshError ->
                                // Check if refresh failed due to network or auth
                                val isRefreshNetworkError = refreshError.message?.contains("Unable to resolve host", ignoreCase = true) == true
                                        || refreshError.message?.contains("timeout", ignoreCase = true) == true
                                        || refreshError.message?.contains("network", ignoreCase = true) == true
                                
                                if (isRefreshNetworkError) {
                                    // Network error - keep optimistic auth
                                    Log.w(TAG, "Network error during refresh (keeping optimistic auth): ${refreshError.message}")
                                    _userState.value = UserState.Error("Network error. Please check your connection.")
                                    // Keep authState as Authenticated
                                } else {
                                    // Clear auth failure - revert to unauthenticated
                                    Log.e(TAG, "Token refresh failed (reverting auth): ${refreshError.message}")
                                    tokenManager.clearTokens()
                                    _authState.value = AuthState.Unauthenticated
                                    _userState.value = UserState.Error("Session expired. Please sign in again.")
                                }
                            }
                    } else {
                        // Unknown error - be conservative and keep optimistic auth
                        // Don't log out user for unknown errors
                        Log.w(TAG, "Unknown error during validation (keeping optimistic auth): ${error.message}")
                        _userState.value = UserState.Error("Validation error: ${error.message}")
                        // Keep authState as Authenticated
                    }
                }
        }
    }

    private fun checkAuthStatus() {
        viewModelScope.launch {
            val accessToken = tokenManager.getAccessToken()
            val refreshToken = tokenManager.getRefreshToken()
            
            Log.d(TAG, "checkAuthStatus: accessToken=${accessToken != null}, refreshToken=${refreshToken != null}")
            
            if (accessToken != null && refreshToken != null) {
                // Tokens exist, validate them using optimistic validation
                // This keeps authState as Authenticated unless there's a clear auth failure
                Log.d(TAG, "checkAuthStatus: Validating tokens optimistically")
                validateTokensOptimistic()
            } else {
                // No tokens, user is not authenticated
                Log.d(TAG, "checkAuthStatus: No tokens found, setting authState to Unauthenticated")
                _authState.value = AuthState.Unauthenticated
            }
        }
    }

    private fun validateTokens() {
        viewModelScope.launch {
            // Try to fetch user details first - Ktor's Auth plugin will auto-refresh if access token is expired
            authApiClient.getUserDetails()
                .onSuccess { userDetails ->
                    // Tokens are valid, user is authenticated
                    Log.d(TAG, "User authenticated successfully")
                    _authState.value = AuthState.Authenticated
                    _userState.value = UserState.Success(userDetails)
                }
                .onFailure { error ->
                    // Check if this is a network error or authentication error
                    val isNetworkError = error.message?.contains("Unable to resolve host", ignoreCase = true) == true
                            || error.message?.contains("timeout", ignoreCase = true) == true
                            || error.message?.contains("network", ignoreCase = true) == true
                            || error.message?.contains("connection", ignoreCase = true) == true
                    
                    if (isNetworkError) {
                        // Network error - don't clear tokens, don't change auth state
                        // User might be offline, tokens are still valid
                        // Keep current auth state (Unknown) so navigation doesn't change
                        Log.w(TAG, "Network error during token validation: ${error.message}")
                        _userState.value = UserState.Error("Network error. Please check your connection.")
                        // Don't change auth state - keep it as Unknown so we can retry later
                        // Don't clear tokens - they might still be valid
                        return@launch
                    }
                    
                    // If fetching user details failed, try manual refresh
                    // This handles cases where Ktor's auto-refresh might not have worked
                    Log.d(TAG, "Failed to fetch user details (${error.message}), attempting manual token refresh")
                    
                    // Try manual refresh as fallback
                    authApiClient.refreshToken()
                        .onSuccess { refreshResponse ->
                            // Refresh successful, now try fetching user details again
                            Log.d(TAG, "Token refresh successful, fetching user details")
                            authApiClient.getUserDetails()
                                .onSuccess { userDetails ->
                                    _authState.value = AuthState.Authenticated
                                    _userState.value = UserState.Success(userDetails)
                                }
                                .onFailure { fetchError ->
                                    // Check if this is also a network error
                                    val isFetchNetworkError = fetchError.message?.contains("Unable to resolve host", ignoreCase = true) == true
                                            || fetchError.message?.contains("timeout", ignoreCase = true) == true
                                            || fetchError.message?.contains("network", ignoreCase = true) == true
                                    
                                    if (isFetchNetworkError) {
                                        // Network error - don't clear tokens, keep auth state
                                        Log.w(TAG, "Network error after refresh: ${fetchError.message}")
                                        _userState.value = UserState.Error("Network error. Please check your connection.")
                                        // Don't change auth state - keep it as Unknown
                                    } else {
                                        // Even after refresh, fetching user details failed - likely auth error
                                        Log.e(TAG, "Failed to fetch user details after refresh: ${fetchError.message}")
                                        tokenManager.clearTokens()
                                        _authState.value = AuthState.Unauthenticated
                                        _userState.value = UserState.Error("Session expired. Please sign in again.")
                                    }
                                }
                        }
                        .onFailure { refreshError ->
                            // Check if refresh failed due to network or auth
                            val isRefreshNetworkError = refreshError.message?.contains("Unable to resolve host", ignoreCase = true) == true
                                    || refreshError.message?.contains("timeout", ignoreCase = true) == true
                                    || refreshError.message?.contains("network", ignoreCase = true) == true
                            
                            if (isRefreshNetworkError) {
                                // Network error - don't clear tokens, keep auth state
                                Log.w(TAG, "Network error during token refresh: ${refreshError.message}")
                                _userState.value = UserState.Error("Network error. Please check your connection.")
                                // Don't change auth state - keep it as Unknown
                            } else {
                                // Refresh failed - tokens are invalid or expired (auth error)
                                Log.d(TAG, "Token refresh failed (auth error): ${refreshError.message}")
                                tokenManager.clearTokens()
                                _authState.value = AuthState.Unauthenticated
                                _userState.value = UserState.Error("Session expired. Please sign in again.")
                            }
                        }
                }
        }
    }

    // This method is no longer needed as validateTokens() now handles refresh directly
    // Keeping it for backward compatibility but it's not used
    @Deprecated("Use validateTokens() instead")
    private fun attemptTokenRefresh() {
        validateTokens()
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
