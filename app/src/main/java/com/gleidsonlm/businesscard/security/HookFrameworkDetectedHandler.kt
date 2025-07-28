package com.gleidsonlm.businesscard.security

import android.util.Log
import com.gleidsonlm.businesscard.model.ThreatEventData
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Handler for HookFrameworkDetected threat events from Appdome.
 * 
 * Detects hook frameworks (e.g., Xposed) which can be used to modify app behavior.
 * Reference: https://www.appdome.com/how-to/mobile-malware-prevention/android-malware-detection/detect-hooking-frameworks/
 */
@Singleton
class HookFrameworkDetectedHandler @Inject constructor() {

    companion object {
        private const val TAG = "HookFrameworkDetected"
    }

    fun handleHookFrameworkDetectedEvent(threatEventData: ThreatEventData) {
        val sanitizedData = "id=${threatEventData.deviceID}, type=${threatEventData.threatCode}"
        Log.w(TAG, "Hook Framework Detected: $sanitizedData")
    }
}