package com.gleidsonlm.businesscard.security

import android.util.Log
import com.gleidsonlm.businesscard.model.ThreatEventData
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Handler for KeyInjectionDetected threat events from Appdome.
 * 
 * Detects keystroke injection attacks which may indicate fraudulent input.
 * Reference: https://www.appdome.com/how-to/mobile-fraud-prevention-detection/know-your-customer-checks/block-keystroke-injection-attacks-protect-android-apps/
 */
@Singleton
class KeyInjectionDetectedHandler @Inject constructor() {

    companion object {
        private const val TAG = "KeyInjectionDetected"
    }

    fun handleKeyInjectionDetectedEvent(threatEventData: ThreatEventData) {
        val sanitizedData = "id=${threatEventData.deviceID}, type=${threatEventData.threatCode}"
        Log.w(TAG, "Key Injection Detected: $sanitizedData")
    }
}