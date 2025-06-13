package com.gleidsonlm.businesscard.ui.viewmodel

import android.util.Log
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gleidsonlm.businesscard.ui.UserData
import com.gleidsonlm.businesscard.data.repository.UserRepository
import com.gleidsonlm.businesscard.util.VCardHelper
import kotlinx.coroutines.Dispatchers
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
            val loadedData = userRepository.loadUserData()
            _currentData.value = loadedData
            loadedData?.let {
                generateQrCode(it)
            }
        }
    }

    fun generateQrCode(userData: UserData) {
        viewModelScope.launch(Dispatchers.Default) {
            Log.d("BusinessCardViewModel", "Generating QR Code for: ${userData.fullName}")
            try {
                val vCardString = VCardHelper.generateVCardString(userData)
                Log.d("BusinessCardViewModel", "vCard String: $vCardString")
                val androidBitmap = VCardHelper.generateQRCodeBitmap(vCardString)
                if (androidBitmap != null) {
                    _qrCodeImageBitmap.value = androidBitmap.asImageBitmap()
                    Log.d("BusinessCardViewModel", "QR Code ImageBitmap updated.")
                } else {
                    _qrCodeImageBitmap.value = null // Explicitly set to null if bitmap generation failed
                    Log.e("BusinessCardViewModel", "QR Code Android Bitmap generation failed.")
                }
            } catch (e: Exception) {
                _qrCodeImageBitmap.value = null // Ensure null on any exception during the process
                Log.e("BusinessCardViewModel", "Exception while generating QR Code", e)
            }
        }
    }
}
