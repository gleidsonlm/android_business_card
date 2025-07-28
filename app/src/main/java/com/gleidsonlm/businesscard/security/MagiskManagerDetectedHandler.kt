package com.gleidsonlm.businesscard.security

import android.util.Log
import com.gleidsonlm.businesscard.model.ThreatEventData
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Handler for MagiskManagerDetected threat events from Appdome.
 * 
 * Detects Magisk Manager installations which provide systemless root access.
 * Reference: https://www.appdome.com/how-to/mobile-malware-prevention/android-malware-detection/block-magisk-magisk-root-in-android-apps/
 */
@Singleton
class MagiskManagerDetectedHandler @Inject constructor() {

    companion object {
        private const val TAG = "MagiskManagerDetected"
    }

    fun handleMagiskManagerDetectedEvent(threatEventData: ThreatEventData) {
        val sanitizedData = "id=${threatEventData.deviceID}, type=${threatEventData.threatCode}"
        Log.w(TAG, "Magisk Manager Detected: $sanitizedData")
    }
}