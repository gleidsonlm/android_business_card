package com.gleidsonlm.businesscard.security

import android.util.Log
import com.gleidsonlm.businesscard.model.ThreatEventData
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Handler for OsRemountDetected threat events from Appdome.
 * 
 * Detects when OS remounts are present which may indicate system partition tampering.
 * Reference: https://www.appdome.com/how-to/mobile-malware-prevention/android-malware-detection/detect-os-remount-in-android/
 */
@Singleton
class OsRemountDetectedHandler @Inject constructor() {

    companion object {
        private const val TAG = "OsRemountDetected"
    }

    fun handleOsRemountDetectedEvent(threatEventData: ThreatEventData) {
        val sanitizedData = "id=${threatEventData.deviceID}, type=${threatEventData.threatCode}"
        Log.w(TAG, "OS Remount Detected: $sanitizedData")
    }
}