package com.gleidsonlm.businesscard.security

import android.util.Log
import com.gleidsonlm.businesscard.model.ThreatEventData
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Handler for SeccompDetected threat events from Appdome.
 * 
 * Detects seccomp abuse which may indicate AI-assisted attacks.
 * Reference: https://www.appdome.com/how-to/mobile-fraud-prevention-detection/know-your-customer-checks/detect-seccomp-abuse-in-android-apps-using-ai/
 */
@Singleton
class SeccompDetectedHandler @Inject constructor() {

    companion object {
        private const val TAG = "SeccompDetected"
    }

    fun handleSeccompDetectedEvent(threatEventData: ThreatEventData) {
        val sanitizedData = "id=${threatEventData.deviceID}, type=${threatEventData.threatCode}"
        Log.w(TAG, "Seccomp Abuse Detected: $sanitizedData")
    }
}