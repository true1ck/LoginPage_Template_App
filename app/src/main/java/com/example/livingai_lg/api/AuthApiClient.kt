package com.example.livingai_lg.api

import android.os.Build
import com.example.livingai_lg.BuildConfig
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.json.Json
import java.util.Locale
import java.util.TimeZone

class AuthApiClient(private val baseUrl: String) {
    private val client = HttpClient {
        install(ContentNegotiation) {
            json(Json {
                ignoreUnknownKeys = true
            })
        }
    }

    suspend fun requestOtp(phoneNumber: String): Result<RequestOtpResponse> {
        return try {
            val response = client.post("$baseUrl/auth/request-otp") {
                contentType(ContentType.Application.Json)
                setBody(RequestOtpRequest(phoneNumber))
            }
            Result.success(response.body())
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun verifyOtp(
        phoneNumber: String,
        code: String,
        deviceId: String
    ): Result<VerifyOtpResponse> {
        return try {
            val request = VerifyOtpRequest(phoneNumber, code, deviceId, getDeviceInfo())
            val response = client.post("$baseUrl/auth/verify-otp") {
                contentType(ContentType.Application.Json)
                setBody(request)
            }
            Result.success(response.body())
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun refreshToken(refreshToken: String): Result<RefreshResponse> {
        return try {
            val request = RefreshRequest(refreshToken)
            val response = client.post("$baseUrl/auth/refresh") {
                contentType(ContentType.Application.Json)
                setBody(request)
            }
            Result.success(response.body())
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun updateProfile(
        name: String,
        userType: String,
        accessToken: String
    ): Result<UpdateProfileResponse> {
        return try {
            val request = UpdateProfileRequest(name, userType)
            val response = client.put("$baseUrl/users/me") {
                contentType(ContentType.Application.Json)
                header("Authorization", "Bearer $accessToken")
                setBody(request)
            }
            Result.success(response.body())
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun logout(refreshToken: String): Result<Unit> {
        return try {
            val request = RefreshRequest(refreshToken)
            client.post("$baseUrl/auth/logout") {
                contentType(ContentType.Application.Json)
                setBody(request)
            }
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    private fun getDeviceInfo(): DeviceInfo {
        return DeviceInfo(
            platform = "android",
            model = Build.MODEL,
            os_version = Build.VERSION.RELEASE,
            app_version = BuildConfig.VERSION_NAME,
            language_code = Locale.getDefault().toString(),
            timezone = TimeZone.getDefault().id
        )
    }
}
