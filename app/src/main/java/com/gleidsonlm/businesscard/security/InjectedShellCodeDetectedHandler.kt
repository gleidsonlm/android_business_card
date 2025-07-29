package com.gleidsonlm.businesscard.security

import android.util.Log
import com.gleidsonlm.businesscard.model.ThreatEventData
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Handler for InjectedShellCodeDetected threat events from Appdome.
 * 
 * Detects injected shell code which may indicate malicious code injection attacks.
 * Reference: https://www.appdome.com/how-to/mobile-malware-prevention/android-malware-detection/block-shell-code/
 */
@Singleton
class InjectedShellCodeDetectedHandler @Inject constructor() {

    companion object {
        private const val TAG = "InjectedShellCode"
    }

    fun handleInjectedShellCodeDetectedEvent(threatEventData: ThreatEventData) {
        val sanitizedData = "id=${threatEventData.deviceID}, type=${threatEventData.threatCode}"
        Log.w(TAG, "Injected Shell Code Detected: $sanitizedData")
    }
}