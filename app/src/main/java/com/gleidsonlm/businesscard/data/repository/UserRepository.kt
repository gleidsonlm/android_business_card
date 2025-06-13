package com.gleidsonlm.businesscard.data.repository

import com.gleidsonlm.businesscard.ui.UserData

interface UserRepository {
    suspend fun saveUserData(userData: UserData)
    suspend fun loadUserData(): UserData?
}
