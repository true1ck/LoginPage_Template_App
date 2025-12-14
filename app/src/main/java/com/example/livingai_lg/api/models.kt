package com.example.livingai_lg.api

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

// region: OTP and Login
@Serializable
data class RequestOtpRequest(@SerialName("phone_number") val phoneNumber: String)

@Serializable
data class RequestOtpResponse(val ok: Boolean)

@Serializable
data class DeviceInfo(
    val platform: String,
    val model: String? = null,
    @SerialName("os_version") val osVersion: String? = null,
    @SerialName("app_version") val appVersion: String? = null,
    @SerialName("language_code") val languageCode: String? = null,
    val timezone: String? = null
)

@Serializable
data class VerifyOtpRequest(
    @SerialName("phone_number") val phoneNumber: String,
    val code: String,
    @SerialName("device_id") val deviceId: String,
    @SerialName("device_info") val deviceInfo: DeviceInfo? = null
)

@Serializable
data class VerifyOtpResponse(
    val user: User,
    @SerialName("access_token") val accessToken: String,
    @SerialName("refresh_token") val refreshToken: String,
    @SerialName("needs_profile") val needsProfile: Boolean
)
// endregion

// region: Token Refresh
@Serializable
data class RefreshRequest(@SerialName("refresh_token") val refreshToken: String)

@Serializable
data class RefreshResponse(
    @SerialName("access_token") val accessToken: String,
    @SerialName("refresh_token") val refreshToken: String
)
// endregion

// region: User Profile
@Serializable
data class UpdateProfileRequest(
    val name: String,
    @SerialName("user_type") val userType: String
)

@Serializable
data class User(
    val id: String,
    @SerialName("phone_number") val phoneNumber: String,
    val name: String?,
    val role: String,
    @SerialName("user_type") val userType: String?
)

@Serializable
data class Location(
    val city: String?,
    val state: String?,
    val pincode: String?
)

@Serializable
data class UserDetails(
    val id: String,
    @SerialName("phone_number") val phoneNumber: String,
    val name: String?,
    @SerialName("user_type") val userType: String?,
    @SerialName("last_login_at") val lastLoginAt: String?,
    val location: Location?,
    val locations: List<Location> = emptyList(),
    @SerialName("active_devices_count") val activeDevicesCount: Int
)
// endregion

// region: Logout
@Serializable
data class LogoutResponse(val ok: Boolean)
// endregion
