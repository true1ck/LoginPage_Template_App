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
                    if (accessToken != null && refreshToken != null) {
                        BearerTokens(accessToken, refreshToken)
                    } else {
                        null
                    }
                }

                refreshTokens {
                    val refreshToken = tokenManager.getRefreshToken() ?: return@refreshTokens null

                    val response: RefreshResponse = client.post("http://10.0.2.2:3000/auth/refresh") {
                        markAsRefreshTokenRequest()
                        contentType(ContentType.Application.Json)
                        setBody(RefreshRequest(refreshToken))
                    }.body()

                    tokenManager.saveTokens(response.accessToken, response.refreshToken)

                    BearerTokens(response.accessToken, response.refreshToken)
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
        client.get("users/me").body()
    }

    suspend fun refreshToken(): Result<RefreshResponse> = runCatching {
        val refreshToken = tokenManager.getRefreshToken() 
            ?: throw IllegalStateException("No refresh token found")
        
        val response: RefreshResponse = client.post("auth/refresh") {
            contentType(ContentType.Application.Json)
            setBody(RefreshRequest(refreshToken))
        }.body()

        // Save the new tokens (refresh token rotates)
        tokenManager.saveTokens(response.accessToken, response.refreshToken)
        response
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
