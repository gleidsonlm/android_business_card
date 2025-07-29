package com.gleidsonlm.businesscard.security

import android.util.Log
import com.gleidsonlm.businesscard.model.ThreatEventData
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Handler for FridaDetected threat events from Appdome.
 * 
 * Detects Frida instrumentation framework which can be used for dynamic analysis and code injection.
 * Reference: https://www.appdome.com/how-to/mobile-malware-prevention/binary-instrumentation-detection/detect-frida-tool-in-mobile-apps/
 */
@Singleton
class FridaDetectedHandler @Inject constructor() {

    companion object {
        private const val TAG = "FridaDetected"
    }

    fun handleFridaDetectedEvent(threatEventData: ThreatEventData) {
        val sanitizedData = "id=${threatEventData.deviceID}, type=${threatEventData.threatCode}"
        Log.w(TAG, "Frida Instrumentation Detected: $sanitizedData")
    }
}