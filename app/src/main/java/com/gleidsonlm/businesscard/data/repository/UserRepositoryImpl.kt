package com.gleidsonlm.businesscard.data.repository

import android.content.Context
import com.gleidsonlm.businesscard.ui.UserData
import com.google.gson.Gson
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(private val context: Context) : UserRepository {

    private val gson = Gson()

    companion object {
        private const val PREFS_NAME = "BusinessCardPrefs"
        private const val USER_DATA_KEY = "user_data_json"
    }

    override suspend fun saveUserData(userData: UserData) {
        val sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        val jsonUserData = gson.toJson(userData)
        editor.putString(USER_DATA_KEY, jsonUserData)
        editor.apply() // apply() is asynchronous, consider commit() if immediate consistency is critical (though apply is generally preferred)
    }

    override suspend fun loadUserData(): UserData? {
        val sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val jsonUserData = sharedPreferences.getString(USER_DATA_KEY, null)
        return if (jsonUserData != null) {
            gson.fromJson(jsonUserData, UserData::class.java)
        } else {
            null
        }
    }
}
