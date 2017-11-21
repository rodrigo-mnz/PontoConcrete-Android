package com.caetano.r.mypoint

import android.content.Context
import android.content.SharedPreferences
import com.caetano.r.mypoint.api.LoginResponse


class UserManager(private val sharedPreferences: SharedPreferences) {

    companion object {
        private val CLIENT_ID = "client_id"
        private val TOKEN = "token"
        private val EMAIL = "email"
        private val PREFERENCE_NAME = "MYPOINT_PREFS"

        fun newInstance(context: Context) =
                UserManager(context.getSharedPreferences(PREFERENCE_NAME,
                        Context.MODE_PRIVATE))
    }

    fun saveUser(loginResponse: LoginResponse) {
        sharedPreferences.edit()
                .putString(CLIENT_ID, loginResponse.client_id)
                .putString(TOKEN, loginResponse.token)
                .putString(EMAIL, loginResponse.data.email)
                .apply()
    }

    fun getSavedUser() : SavedUser {
        val clientId = sharedPreferences.getString(CLIENT_ID, "")
        val token = sharedPreferences.getString(TOKEN, "")
        val email = sharedPreferences.getString(EMAIL, "")

        return SavedUser(token, clientId, email)
    }

    fun hasSavedUser(): Boolean = !sharedPreferences.getString(TOKEN, "").isEmpty()

}