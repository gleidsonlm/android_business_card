package com.gleidsonlm.businesscard.security

import android.util.Log
import com.gleidsonlm.businesscard.model.ThreatEventData
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Handler for OverlayDetected threat events from Appdome.
 * 
 * Detects overlay attacks or malware that may steal sensitive information or manipulate user interface.
 * Reference: https://www.appdome.com/how-to/account-takeover-prevention/account-protection/protect-android-apps-from-overlay-attacks-malware/
 */
@Singleton
class OverlayDetectedHandler @Inject constructor() {

    companion object {
        private const val TAG = "OverlayDetected"
    }

    fun handleOverlayDetectedEvent(threatEventData: ThreatEventData) {
        val sanitizedData = "id=${threatEventData.deviceID}, type=${threatEventData.threatCode}"
        Log.w(TAG, "Overlay Attack Detected: $sanitizedData")
    }
}