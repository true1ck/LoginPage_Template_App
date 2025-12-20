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
    val code: Int,
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

// region: Signup
@Serializable
data class SignupRequest(
    val name: String,
    @SerialName("phone_number") val phoneNumber: String,
    val state: String? = null,
    val district: String? = null,
    @SerialName("city_village") val cityVillage: String? = null,
    @SerialName("device_id") val deviceId: String? = null,
    @SerialName("device_info") val deviceInfo: DeviceInfo? = null
)

@Serializable
data class SignupResponse(
    val success: Boolean,
    val user: User? = null,
    @SerialName("access_token") val accessToken: String? = null,
    @SerialName("refresh_token") val refreshToken: String? = null,
    @SerialName("needs_profile") val needsProfile: Boolean? = null,
    @SerialName("is_new_account") val isNewAccount: Boolean? = null,
    @SerialName("is_new_device") val isNewDevice: Boolean? = null,
    @SerialName("active_devices_count") val activeDevicesCount: Int? = null,
    @SerialName("location_id") val locationId: String? = null,
    val message: String? = null,
    @SerialName("user_exists") val userExists: Boolean? = null
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
    val role: String? = null,  // Optional field - can be missing from JSON, defaults to null
    @SerialName("user_type") val userType: String? = null,  // Optional field - can be missing from JSON, defaults to null
    @SerialName("created_at") val createdAt: String? = null,
    @SerialName("country_code") val countryCode: String? = null
)

@Serializable
data class Location(
    @SerialName("city_village") val cityVillage: String? = null,
    val state: String? = null,
    val pincode: String? = null
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

// region: Error Responses
@Serializable
data class ErrorResponse(
    val success: Boolean? = null,
    val error: String? = null,
    val message: String? = null,
    @SerialName("user_exists") val userExists: Boolean? = null
)
// endregion

// region: User Check
@Serializable
data class CheckUserRequest(@SerialName("phone_number") val phoneNumber: String)

@Serializable
data class CheckUserResponse(
    val success: Boolean,
    val message: String? = null,
    @SerialName("user_exists") val userExists: Boolean
)
// endregion
