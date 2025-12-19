package com.example.livingai_lg.api

import android.content.Context
import android.os.Build
import android.provider.Settings
import com.example.livingai_lg.BuildConfig
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.auth.*
import io.ktor.client.plugins.auth.providers.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.json.Json
import java.util.Locale
import java.util.TimeZone

// Custom exception for user not found
class UserNotFoundException(
    message: String,
    val errorCode: String
) : Exception(message)

class AuthApiClient(private val context: Context) {

    private val tokenManager = TokenManager(context)

    val client = HttpClient(CIO) {

        install(ContentNegotiation) {
            json(Json {
                prettyPrint = true
                isLenient = true
                ignoreUnknownKeys = true
            })
        }


        install(Auth) {
            bearer {
                loadTokens {
                    val accessToken = tokenManager.getAccessToken()
                    val refreshToken = tokenManager.getRefreshToken()
                    android.util.Log.d("AuthApiClient", "loadTokens: accessToken=${accessToken != null}, refreshToken=${refreshToken != null}")
                    if (accessToken != null && refreshToken != null) {
                        android.util.Log.d("AuthApiClient", "loadTokens: Returning BearerTokens")
                        BearerTokens(accessToken, refreshToken)
                    } else {
                        android.util.Log.d("AuthApiClient", "loadTokens: No tokens available, returning null")
                        null
                    }
                }

                refreshTokens {
                    android.util.Log.d("AuthApiClient", "refreshTokens: Starting token refresh")
                    val refreshToken = tokenManager.getRefreshToken() ?: run {
                        android.util.Log.e("AuthApiClient", "refreshTokens: No refresh token found!")
                        return@refreshTokens null
                    }
                    
                    android.util.Log.d("AuthApiClient", "refreshTokens: Calling /auth/refresh endpoint")
                    try {
                        val response: RefreshResponse = client.post("http://10.0.2.2:3000/auth/refresh") {
                            markAsRefreshTokenRequest()
                            contentType(ContentType.Application.Json)
                            setBody(RefreshRequest(refreshToken))
                        }.body()

                        android.util.Log.d("AuthApiClient", "refreshTokens: Refresh successful, saving new tokens")
                        tokenManager.saveTokens(response.accessToken, response.refreshToken)
                        android.util.Log.d("AuthApiClient", "refreshTokens: New tokens saved successfully")

                        BearerTokens(response.accessToken, response.refreshToken)
                    } catch (e: Exception) {
                        android.util.Log.e("AuthApiClient", "refreshTokens: Refresh failed: ${e.message}", e)
                        throw e
                    }
                }
            }
        }

        defaultRequest {
            url("http://10.0.2.2:3000/")
        }
    }

    // --- API Calls ---

    suspend fun checkUser(phoneNumber: String): Result<CheckUserResponse> = runCatching {
        val response = client.post("auth/check-user") {
            contentType(ContentType.Application.Json)
            setBody(CheckUserRequest(phoneNumber))
        }
        
        if (response.status.isSuccess()) {
            // Success - parse as CheckUserResponse
            response.body<CheckUserResponse>()
        } else {
            // Error - parse as ErrorResponse
            val errorResponse = try {
                response.body<ErrorResponse>()
            } catch (e: Exception) {
                // If parsing fails, create default error
                ErrorResponse(
                    success = false,
                    error = "USER_NOT_FOUND",
                    message = "User is not registered. Please sign up to create a new account.",
                    userExists = false
                )
            }
            
            throw UserNotFoundException(
                message = errorResponse.message ?: "User is not registered. Please sign up to create a new account.",
                errorCode = errorResponse.error ?: "USER_NOT_FOUND"
            )
        }
    }

    suspend fun requestOtp(phoneNumber: String): Result<RequestOtpResponse> = runCatching {
        client.post("auth/request-otp") {
            contentType(ContentType.Application.Json)
            setBody(RequestOtpRequest(phoneNumber))
        }.body()
    }

    suspend fun verifyOtp(phoneNumber: String, code: String, deviceId: String): Result<VerifyOtpResponse> = runCatching {
        val response = client.post("auth/verify-otp") {
            contentType(ContentType.Application.Json)
            setBody(VerifyOtpRequest(phoneNumber, code.toInt(), deviceId, getDeviceInfo()))
        }
        
        if (response.status.isSuccess()) {
            val verifyResponse: VerifyOtpResponse = response.body()
            tokenManager.saveTokens(verifyResponse.accessToken, verifyResponse.refreshToken)
            verifyResponse
        } else {
            // Parse error response
            val errorResponse: ErrorResponse = response.body()
            throw UserNotFoundException(
                message = errorResponse.message ?: "User not found",
                errorCode = errorResponse.error ?: "USER_NOT_FOUND"
            )
        }
    }

    suspend fun signup(request: SignupRequest): Result<SignupResponse> = runCatching {
        val response = client.post("auth/signup") {
            contentType(ContentType.Application.Json)
            setBody(request.copy(deviceId = getDeviceId(), deviceInfo = getDeviceInfo()))
        }

        // Instead of throwing an exception on non-2xx, we return a result type
        // that can be handled by the caller.
        if (response.status.isSuccess()) {
            response.body<SignupResponse>()
        } else {
            response.body<SignupResponse>()
        }
    }

    suspend fun updateProfile(name: String, userType: String): Result<User> = runCatching {
        client.put("users/me") {
            contentType(ContentType.Application.Json)
            setBody(UpdateProfileRequest(name, userType))
        }.body()
    }

    suspend fun getUserDetails(): Result<UserDetails> = runCatching {
        android.util.Log.d("AuthApiClient", "getUserDetails: Calling /users/me endpoint")
        try {
            val response = client.get("users/me")
            android.util.Log.d("AuthApiClient", "getUserDetails: Response status=${response.status}")
            val userDetails = response.body<UserDetails>()
            android.util.Log.d("AuthApiClient", "getUserDetails: Success - user id=${userDetails.id}")
            userDetails
        } catch (e: Exception) {
            android.util.Log.e("AuthApiClient", "getUserDetails: Error - ${e.message}", e)
            throw e
        }
    }

    suspend fun refreshToken(): Result<RefreshResponse> = runCatching {
        android.util.Log.d("AuthApiClient", "refreshToken: Starting manual token refresh")
        val refreshToken = tokenManager.getRefreshToken() 
            ?: throw IllegalStateException("No refresh token found")
        
        android.util.Log.d("AuthApiClient", "refreshToken: Calling /auth/refresh endpoint")
        try {
            val response: RefreshResponse = client.post("auth/refresh") {
                contentType(ContentType.Application.Json)
                setBody(RefreshRequest(refreshToken))
            }.body()

            android.util.Log.d("AuthApiClient", "refreshToken: Refresh successful, saving new tokens")
            // Save the new tokens (refresh token rotates)
            tokenManager.saveTokens(response.accessToken, response.refreshToken)
            android.util.Log.d("AuthApiClient", "refreshToken: New tokens saved successfully")
            response
        } catch (e: Exception) {
            android.util.Log.e("AuthApiClient", "refreshToken: Refresh failed: ${e.message}", e)
            throw e
        }
    }

    suspend fun logout(): Result<LogoutResponse> = runCatching {
        val refreshToken = tokenManager.getRefreshToken() ?: throw IllegalStateException("No refresh token found")
        val response: LogoutResponse = client.post("auth/logout") {
            contentType(ContentType.Application.Json)
            setBody(RefreshRequest(refreshToken))
        }.body()

        tokenManager.clearTokens()
        response
    }

    private fun getDeviceInfo(): DeviceInfo {
        return DeviceInfo(
            platform = "android",
            model = Build.MODEL,
            osVersion = Build.VERSION.RELEASE,
            appVersion = BuildConfig.VERSION_NAME,
            languageCode = Locale.getDefault().toString(),
            timezone = TimeZone.getDefault().id
        )
    }

    private fun getDeviceId(): String {
        return Settings.Secure.getString(context.contentResolver, Settings.Secure.ANDROID_ID)
    }
}
