package com.gleidsonlm.businesscard.util

import android.content.Context
import com.gleidsonlm.businesscard.ui.UserData
import com.google.gson.Gson

object DataStore {

    private const val PREFS_NAME = "BusinessCardPrefs"
    private const val USER_DATA_KEY = "user_data_json"

    fun saveUserData(context: Context, userData: UserData) {
        val sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        val gson = Gson()
        val jsonUserData = gson.toJson(userData)
        editor.putString(USER_DATA_KEY, jsonUserData)
        editor.apply()
    }

    fun loadUserData(context: Context): UserData? {
        val sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val jsonUserData = sharedPreferences.getString(USER_DATA_KEY, null)
        return if (jsonUserData != null) {
            val gson = Gson()
            gson.fromJson(jsonUserData, UserData::class.java)
        } else {
            null
        }
    }
}
