package com.example.livingai_lg.api

import android.content.Context
import android.provider.Settings

class AuthManager(
    private val context: Context,
    private val apiClient: AuthApiClient,
    private val tokenManager: TokenManager
) {
    suspend fun requestOtp(phoneNumber: String): Result<RequestOtpResponse> {
        return apiClient.requestOtp(phoneNumber)
    }

    suspend fun login(phoneNumber: String, code: String): Result<VerifyOtpResponse> {
        val deviceId = getDeviceId()
        return apiClient.verifyOtp(phoneNumber, code, deviceId)
            .onSuccess { response ->
                tokenManager.saveTokens(response.accessToken, response.refreshToken)
            }
    }

    suspend fun updateProfile(name: String, userType: String): Result<User> {
        return apiClient.updateProfile(name, userType)
    }

    private fun getDeviceId(): String {
        return Settings.Secure.getString(
            context.contentResolver,
            Settings.Secure.ANDROID_ID
        )
    }
}
