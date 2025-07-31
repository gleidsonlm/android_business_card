package com.gleidsonlm.businesscard.security

import android.util.Log
import com.gleidsonlm.businesscard.model.ThreatEventData
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Handler for OatIntegrityBadCommandLine threat events from Appdome.
 * 
 * Detects APK modding tools through OAT integrity checks which may indicate tampering.
 * Reference: https://www.appdome.com/how-to/mobile-cheat-prevention/mobile-app-modding-detection/detect-apk-modding-tool-in-android-apps/
 */
@Singleton
class OatIntegrityBadCommandLineHandler @Inject constructor() {

    companion object {
        private const val TAG = "OatIntegrityBadCommandLine"
    }

    fun handleOatIntegrityBadCommandLineEvent(threatEventData: ThreatEventData) {
        val sanitizedData = "id=${threatEventData.deviceID}, type=${threatEventData.threatCode}"
        Log.w(TAG, "OAT Integrity Bad Command Line: $sanitizedData")
    }
}