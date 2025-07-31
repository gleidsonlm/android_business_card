package com.gleidsonlm.businesscard.security

import android.util.Log
import com.gleidsonlm.businesscard.model.ThreatEventData
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Handler for CodeInjectionDetected threat events from Appdome.
 * 
 * Detects code and process injection attacks which may indicate malicious modifications.
 * Reference: https://www.appdome.com/how-to/mobile-cheat-prevention/prevent-injection-attacks/prevent-code-injection-process-injection-attacks-in-android-apps/
 */
@Singleton
class CodeInjectionDetectedHandler @Inject constructor() {

    companion object {
        private const val TAG = "CodeInjectionDetected"
    }

    fun handleCodeInjectionDetectedEvent(threatEventData: ThreatEventData) {
        val sanitizedData = "id=${threatEventData.deviceID}, type=${threatEventData.threatCode}"
        Log.w(TAG, "Code Injection Detected: $sanitizedData")
    }
}