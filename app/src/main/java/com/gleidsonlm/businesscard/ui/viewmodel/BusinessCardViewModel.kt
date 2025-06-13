package com.gleidsonlm.businesscard.ui.viewmodel

import androidx.compose.ui.graphics.ImageBitmap
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gleidsonlm.businesscard.ui.UserData
import com.gleidsonlm.businesscard.data.repository.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class BusinessCardViewModel(private val userRepository: UserRepository) : ViewModel() {

    private val _currentData = MutableStateFlow<UserData?>(null)
    val currentData: StateFlow<UserData?> = _currentData

    private val _qrCodeImageBitmap = MutableStateFlow<ImageBitmap?>(null)
    val qrCodeImageBitmap: StateFlow<ImageBitmap?> = _qrCodeImageBitmap

    fun loadInitialData() {
        viewModelScope.launch {
            _currentData.value = userRepository.loadUserData()
        }
    }

    fun generateQrCode(userData: UserData) {
        // Will be implemented later
    }
}
