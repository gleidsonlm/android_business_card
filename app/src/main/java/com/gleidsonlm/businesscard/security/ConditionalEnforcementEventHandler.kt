package com.gleidsonlm.businesscard.security

import android.util.Log
import com.gleidsonlm.businesscard.model.ThreatEventData
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Handler for ConditionalEnforcementEvent threat events from Appdome.
 * 
 * Uses conditional enforcement for threat response, allowing customized responses based on threat conditions.
 * Reference: https://www.appdome.com/how-to/advanced-threat-intelligence-android-ios/threat-events-ux-ui-control/use-conditional-enforcement-in-mobile-apps/
 */
@Singleton
class ConditionalEnforcementEventHandler @Inject constructor() {

    companion object {
        private const val TAG = "ConditionalEnforcementEvent"
    }

    /**
     * Handles the ConditionalEnforcementEvent threat event.
     * 
     * This event is triggered when conditional enforcement rules are applied, allowing
     * for sophisticated threat response policies that can vary based on threat severity,
     * user context, device characteristics, or other conditional factors.
     * 
     * @param threatEventData The comprehensive threat event data from Appdome
     */
    fun handleConditionalEnforcementEventEvent(threatEventData: ThreatEventData) {
        val sanitizedData = "id=${threatEventData.deviceID}, type=${threatEventData.threatCode}"
        Log.i(TAG, "Conditional Enforcement Applied: $sanitizedData")
        
        // Log enforcement context and conditions
        threatEventData.failSafeEnforce?.let { enforcement ->
            Log.i(TAG, "Enforcement action: $enforcement")
        }
        
        threatEventData.reasonCode?.let { reason ->
            Log.i(TAG, "Enforcement reason: $reason")
        }
        
        threatEventData.message?.let { message ->
            Log.i(TAG, "Conditional enforcement details: ${message.take(100)}")
        }
        
        // Note: Conditional enforcement allows for nuanced threat responses
        // that can be tailored to specific scenarios rather than applying blanket policies.
    }
}