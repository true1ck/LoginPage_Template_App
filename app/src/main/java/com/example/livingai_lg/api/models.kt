package com.example.livingai_lg.api

import kotlinx.serialization.Serializable

@Serializable
data class RequestOtpRequest(val phone_number: String)

@Serializable
data class RequestOtpResponse(val ok: Boolean)

@Serializable
data class DeviceInfo(
    val platform: String,
    val model: String? = null,
    val os_version: String? = null,
    val app_version: String? = null,
    val language_code: String? = null,
    val timezone: String? = null
)

@Serializable
data class VerifyOtpRequest(
    val phone_number: String,
    val code: String,
    val device_id: String,
    val device_info: DeviceInfo? = null
)

@Serializable
data class User(
    val id: String,
    val phone_number: String,
    val name: String?,
    val role: String,
    val user_type: String?
)

@Serializable
data class VerifyOtpResponse(
    val user: User,
    val access_token: String,
    val refresh_token: String,
    val needs_profile: Boolean
)

@Serializable
data class RefreshRequest(val refresh_token: String)

@Serializable
data class RefreshResponse(val access_token: String, val refresh_token: String)

@Serializable
data class UpdateProfileRequest(val name: String, val user_type: String)

@Serializable
data class UpdateProfileResponse(
    val id: String,
    val phone_number: String,
    val name: String?,
    val role: String,
    val user_type: String?
)