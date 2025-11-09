package com.az.umirhackapp.server.auth

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit

class TokenService(private val context: Context) {
    private val sharedPreferences: SharedPreferences by lazy {
        context.getSharedPreferences("auth_prefs", Context.MODE_PRIVATE)
    }

    companion object {
        private const val KEY_AUTH_TOKEN = "auth_token"
        private const val KEY_REFRESH_TOKEN = "refresh_token"
        private const val KEY_USER_ID = "user_id"
    }

    fun saveAuthToken(token: String) {
        sharedPreferences.edit() { putString(KEY_AUTH_TOKEN, token) }
    }

    fun getAuthToken(): String? {
        return sharedPreferences.getString(KEY_AUTH_TOKEN, null)
    }

    fun saveRefreshToken(token: String) {
        sharedPreferences.edit() { putString(KEY_REFRESH_TOKEN, token) }
    }

    fun getRefreshToken(): String? {
        return sharedPreferences.getString(KEY_REFRESH_TOKEN, null)
    }

    fun saveUserId(userId: String) {
        sharedPreferences.edit() { putString(KEY_USER_ID, userId) }
    }

    fun getUserId(): String? {
        return sharedPreferences.getString(KEY_USER_ID, null)
    }

    fun clearTokens() {
        sharedPreferences.edit() {
            remove(KEY_AUTH_TOKEN)
            remove(KEY_REFRESH_TOKEN)
            remove(KEY_USER_ID)
        }
    }

    fun isLoggedIn(): Boolean {
        return !getAuthToken().isNullOrEmpty()
    }
}