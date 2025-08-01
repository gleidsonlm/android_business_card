package com.gleidsonlm.businesscard.security

import android.util.Log
import com.gleidsonlm.businesscard.model.ThreatEventData
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GoogleEmulatorHandler @Inject constructor() {

    companion object {
        private const val TAG = "GoogleEmulatorHandler"
    }

    fun handleGoogleEmulatorEvent(threatEventData: ThreatEventData) {
        val sanitizedData = "id=${threatEventData.deviceID}, type=${threatEventData.threatCode}"
        Log.w(TAG, "Google Emulator Detected: $sanitizedData")
    }
}
