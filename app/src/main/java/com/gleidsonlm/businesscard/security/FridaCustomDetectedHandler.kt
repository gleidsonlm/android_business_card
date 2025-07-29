package com.gleidsonlm.businesscard.security

import android.util.Log
import com.gleidsonlm.businesscard.model.ThreatEventData
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Handler for FridaCustomDetected threat events from Appdome.
 * 
 * Detects strong/custom Frida detection for advanced instrumentation frameworks.
 * Reference: https://www.appdome.com/how-to/mobile-malware-prevention/binary-instrumentation-detection/detect-strong-frida-in-android-ios-mobile-apps/
 */
@Singleton
class FridaCustomDetectedHandler @Inject constructor() {

    companion object {
        private const val TAG = "FridaCustomDetected"
    }

    fun handleFridaCustomDetectedEvent(threatEventData: ThreatEventData) {
        val sanitizedData = "id=${threatEventData.deviceID}, type=${threatEventData.threatCode}"
        Log.w(TAG, "Custom/Strong Frida Detected: $sanitizedData")
    }
}