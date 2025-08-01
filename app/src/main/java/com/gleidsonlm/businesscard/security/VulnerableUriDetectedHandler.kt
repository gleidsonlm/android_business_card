package com.gleidsonlm.businesscard.security

import android.util.Log
import com.gleidsonlm.businesscard.model.ThreatEventData
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Handler for VulnerableUriDetected threat events from Appdome.
 * 
 * Detects usage of malicious or vulnerable URIs indicating potential security threats.
 * Reference: https://www.appdome.com/how-to/mobile-malware-prevention/android-malware-detection/prevent-stream-manipulation/
 */
@Singleton
class VulnerableUriDetectedHandler @Inject constructor() {

    companion object {
        private const val TAG = "VulnerableUriDetected"
    }

    fun handleVulnerableUriDetectedEvent(threatEventData: ThreatEventData) {
        val sanitizedData = "id=${threatEventData.deviceID}, type=${threatEventData.threatCode}"
        Log.w(TAG, "Vulnerable URI Detected: $sanitizedData")
    }
}