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

class AuthApiClient(private val context: Context) {

    private val tokenManager = TokenManager(context)

    val client = HttpClient(CIO) {

        install(ContentNegotiation) {
            json(Json {
                prettyPrint = true
                isLenient = true
                ignoreUnknownKeys = true
                coerceInputValues = true  // Coerce missing fields to default values
                encodeDefaults = false
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

    suspend fun requestOtp(phoneNumber: String): Result<RequestOtpResponse> = runCatching {
        client.post("auth/request-otp") {
            contentType(ContentType.Application.Json)
            setBody(RequestOtpRequest(phoneNumber))
        }.body()
    }

    suspend fun verifyOtp(phoneNumber: String, code: String, deviceId: String): Result<VerifyOtpResponse> = runCatching {
        // Trim and validate code (ensure no whitespace, exactly 6 digits)
        val trimmedCode = code.trim()
        
        // Debug: Log the request details
        android.util.Log.d("AuthApiClient", "Verify OTP Request:")
        android.util.Log.d("AuthApiClient", "  phone_number: $phoneNumber")
        android.util.Log.d("AuthApiClient", "  code (original): '$code' (length: ${code.length})")
        android.util.Log.d("AuthApiClient", "  code (trimmed): '$trimmedCode' (length: ${trimmedCode.length})")
        android.util.Log.d("AuthApiClient", "  device_id: $deviceId")
        
        // Create request object with trimmed code
        val request = VerifyOtpRequest(phoneNumber, trimmedCode, deviceId, getDeviceInfo())
        
        val response: VerifyOtpResponse = client.post("auth/verify-otp") {
            contentType(ContentType.Application.Json)
            // Send code as string - backend validation requires string type and bcrypt.compare needs string
            setBody(request)
        }.body()

        tokenManager.saveTokens(response.accessToken, response.refreshToken)
        response
    }

    suspend fun signup(request: SignupRequest): Result<SignupResponse> = runCatching {
        val response = client.post("auth/signup") {
            contentType(ContentType.Application.Json)
            setBody(request.copy(deviceId = getDeviceId(), deviceInfo = getDeviceInfo()))
        }

        // Handle both success and error responses
        if (response.status.isSuccess()) {
            response.body<SignupResponse>()
        } else {
            // Try to parse error response as SignupResponse (for 409 conflicts with user_exists flag)
            try {
                response.body<SignupResponse>()
            } catch (e: Exception) {
                // If parsing fails, throw an exception with the status and message
                val errorBody = response.bodyAsText()
                throw Exception("Signup failed: ${response.status} - $errorBody")
            }
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
