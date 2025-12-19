package com.example.livingai_lg.api

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey

class TokenManager(context: Context) {

    private val masterKey = MasterKey.Builder(context)
        .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
        .build()

    private val prefs: SharedPreferences = EncryptedSharedPreferences.create(
        context,
        "secure_auth_prefs",
        masterKey,
        EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
        EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
    )

    companion object {
        private const val TAG = "TokenManager"
        private const val KEY_ACCESS_TOKEN = "access_token"
        private const val KEY_REFRESH_TOKEN = "refresh_token"
    }

    fun saveTokens(accessToken: String, refreshToken: String) {
        Log.d(TAG, "saveTokens: Saving tokens (accessToken length=${accessToken.length}, refreshToken length=${refreshToken.length})")
        val success = prefs.edit()
            .putString(KEY_ACCESS_TOKEN, accessToken)
            .putString(KEY_REFRESH_TOKEN, refreshToken)
            .commit() // Use commit() instead of apply() to ensure tokens are saved synchronously
        
        if (success) {
            Log.d(TAG, "saveTokens: Tokens saved successfully")
            // Verify tokens were saved
            val savedAccess = prefs.getString(KEY_ACCESS_TOKEN, null)
            val savedRefresh = prefs.getString(KEY_REFRESH_TOKEN, null)
            Log.d(TAG, "saveTokens: Verification - accessToken saved=${savedAccess != null}, refreshToken saved=${savedRefresh != null}")
        } else {
            Log.e(TAG, "saveTokens: FAILED to save tokens!")
        }
    }

    fun getAccessToken(): String? {
        val token = prefs.getString(KEY_ACCESS_TOKEN, null)
        Log.d(TAG, "getAccessToken: token=${token != null}, length=${token?.length ?: 0}")
        return token
    }

    fun getRefreshToken(): String? {
        val token = prefs.getString(KEY_REFRESH_TOKEN, null)
        Log.d(TAG, "getRefreshToken: token=${token != null}, length=${token?.length ?: 0}")
        return token
    }

    fun clearTokens() {
        Log.d(TAG, "clearTokens: Clearing all tokens")
        prefs.edit()
            .remove(KEY_ACCESS_TOKEN)
            .remove(KEY_REFRESH_TOKEN)
            .apply()
        Log.d(TAG, "clearTokens: Tokens cleared")
    }
}
