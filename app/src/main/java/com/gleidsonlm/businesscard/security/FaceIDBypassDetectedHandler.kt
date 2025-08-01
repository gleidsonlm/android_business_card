package com.gleidsonlm.businesscard.security

import android.util.Log
import com.gleidsonlm.businesscard.model.ThreatEventData
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Handler for FaceIDBypassDetected threat events from Appdome.
 * 
 * Detects attempts to bypass FaceID authentication using deepfake or spoofing techniques.
 * Reference: https://www.appdome.com/how-to/account-takeover-prevention/deepfake-detection/detect-deepfake/
 */
@Singleton
class FaceIDBypassDetectedHandler @Inject constructor() {

    companion object {
        private const val TAG = "FaceIDBypassDetected"
    }

    fun handleFaceIDBypassDetectedEvent(threatEventData: ThreatEventData) {
        val sanitizedData = "id=${threatEventData.deviceID}, type=${threatEventData.threatCode}"
        Log.w(TAG, "FaceID Bypass Detected: $sanitizedData")
    }
}