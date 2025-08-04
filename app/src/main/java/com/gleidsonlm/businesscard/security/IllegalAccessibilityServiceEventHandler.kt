package com.gleidsonlm.businesscard.security

import android.util.Log
import com.gleidsonlm.businesscard.model.ThreatEventData
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Handler for IllegalAccessibilityServiceEvent threat events from Appdome.
 * 
 * Detects and blocks suspicious accessibility services that may be used for social engineering attacks.
 * Reference: https://www.appdome.com/how-to/account-takeover-prevention/social-engineering-prevention/social-engineering-prevention-blocking-suspicious-accessibility-services/
 */
@Singleton
class IllegalAccessibilityServiceEventHandler @Inject constructor() {

    companion object {
        private const val TAG = "IllegalAccessibilityServiceEvent"
    }

    /**
     * Handles the IllegalAccessibilityServiceEvent threat event.
     * 
     * This event is triggered when suspicious accessibility services are detected that could
     * potentially be used for social engineering attacks or unauthorized access to app functionality.
     * 
     * @param threatEventData The comprehensive threat event data from Appdome
     */
    fun handleIllegalAccessibilityServiceEventEvent(threatEventData: ThreatEventData) {
        val sanitizedData = "id=${threatEventData.deviceID}, type=${threatEventData.threatCode}"
        Log.w(TAG, "Illegal Accessibility Service Detected: $sanitizedData")
        
        // Log additional context if available
        threatEventData.message?.let { message ->
            Log.w(TAG, "Accessibility service threat details: ${message.take(100)}")
        }
        
        // Note: Appdome handles the blocking/enforcement automatically.
        // This handler provides logging and could be extended for custom responses.
    }
}