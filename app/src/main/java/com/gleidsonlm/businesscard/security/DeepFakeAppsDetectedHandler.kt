package com.gleidsonlm.businesscard.security

import android.util.Log
import com.gleidsonlm.businesscard.model.ThreatEventData
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Handler for DeepFakeAppsDetected threat events from Appdome.
 * 
 * Detects presence of deepfake applications that may be used for fraud or impersonation.
 * Reference: https://www.appdome.com/how-to/account-takeover-prevention/deepfake-detection/detect-deepfake-apps-in-android-and-ios/
 */
@Singleton
class DeepFakeAppsDetectedHandler @Inject constructor() {

    companion object {
        private const val TAG = "DeepFakeAppsDetected"
    }

    fun handleDeepFakeAppsDetectedEvent(threatEventData: ThreatEventData) {
        val sanitizedData = "id=${threatEventData.deviceID}, type=${threatEventData.threatCode}"
        Log.w(TAG, "DeepFake Apps Detected: $sanitizedData")
    }
}