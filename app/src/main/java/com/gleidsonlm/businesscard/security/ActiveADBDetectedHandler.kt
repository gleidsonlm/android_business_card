package com.gleidsonlm.businesscard.security

import android.util.Log
import com.gleidsonlm.businesscard.model.ThreatEventData
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Handler for ActiveADBDetected threat events from Appdome.
 * 
 * Detects active Android Debug Bridge connections which may indicate debugging attacks.
 * Reference: https://www.appdome.com/how-to/mobile-fraud-prevention-detection/know-your-customer-checks/block-android-debug-bridge-adb-exploits-protect-android-apps/
 */
@Singleton
class ActiveADBDetectedHandler @Inject constructor() {

    companion object {
        private const val TAG = "ActiveADBDetected"
    }

    fun handleActiveADBDetectedEvent(threatEventData: ThreatEventData) {
        val sanitizedData = "id=${threatEventData.deviceID}, type=${threatEventData.threatCode}"
        Log.w(TAG, "Active ADB Detected: $sanitizedData")
    }
}