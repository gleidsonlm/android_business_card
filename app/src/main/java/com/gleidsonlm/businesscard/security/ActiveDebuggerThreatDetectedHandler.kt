package com.gleidsonlm.businesscard.security

import android.util.Log
import com.gleidsonlm.businesscard.model.ThreatEventData
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Handler for ActiveDebuggerThreatDetected threat events from Appdome.
 * 
 * Detects active debuggers or memory editing tools that may be used for malicious purposes.
 * Reference: https://www.appdome.com/how-to/account-takeover-prevention/account-protection/detect-memory-editing-tools-for-android-and-ios-apps/
 */
@Singleton
class ActiveDebuggerThreatDetectedHandler @Inject constructor() {

    companion object {
        private const val TAG = "ActiveDebuggerThreatDetected"
    }

    /**
     * Handles the ActiveDebuggerThreatDetected threat event.
     * 
     * This event is triggered when active debuggers or memory editing tools are detected
     * that could be used to manipulate app behavior, extract sensitive data, or bypass security measures.
     * 
     * @param threatEventData The comprehensive threat event data from Appdome
     */
    fun handleActiveDebuggerThreatDetectedEvent(threatEventData: ThreatEventData) {
        val sanitizedData = "id=${threatEventData.deviceID}, type=${threatEventData.threatCode}"
        Log.e(TAG, "Active Debugger/Memory Editor Detected: $sanitizedData")
        
        // Log additional technical details if available
        threatEventData.kernelInfo?.let { kernel ->
            Log.e(TAG, "Kernel context: ${kernel.take(50)}")
        }
        
        threatEventData.reasonCode?.let { reason ->
            Log.e(TAG, "Detection reason: $reason")
        }
        
        // Note: This is a high-severity threat that typically requires immediate response.
        // Appdome can be configured to terminate the app or apply other protective measures.
    }
}