package com.gleidsonlm.businesscard.security

import android.util.Log
import com.gleidsonlm.businesscard.model.ThreatEventData
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Handler for DetectUnlockedBootloader threat events from Appdome.
 * 
 * Detects devices with unlocked bootloaders which may indicate compromised device security.
 * Reference: https://www.appdome.com/how-to/mobile-malware-prevention/android-malware-detection/detect-unlocked-bootloader-android/
 */
@Singleton
class DetectUnlockedBootloaderHandler @Inject constructor() {

    companion object {
        private const val TAG = "UnlockedBootloader"
    }

    fun handleDetectUnlockedBootloaderEvent(threatEventData: ThreatEventData) {
        val sanitizedData = "id=${threatEventData.deviceID}, type=${threatEventData.threatCode}"
        Log.w(TAG, "Unlocked Bootloader Detected: $sanitizedData")
    }
}