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
 * 
 * IMPORTANT: Conditional Enforcement is designed to NOT automatically enforce (terminate) the app.
 * Instead, it provides conditional responses based on threat severity and context.
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
     * @return ConditionalEnforcementResult indicating the enforcement decision
     */
    fun handleConditionalEnforcementEventEvent(threatEventData: ThreatEventData): ConditionalEnforcementResult {
        val sanitizedData = "id=${threatEventData.deviceID}, type=${threatEventData.threatCode}"
        Log.i(TAG, "Conditional Enforcement Applied: $sanitizedData")
        
        // Analyze enforcement flags to determine appropriate response
        val shouldEnforce = analyzeEnforcementDecision(threatEventData)
        
        // Log enforcement context and conditions
        threatEventData.failSafeEnforce?.let { enforcement ->
            Log.i(TAG, "Enforcement action specified: $enforcement")
        }
        
        threatEventData.reasonCode?.let { reason ->
            Log.i(TAG, "Enforcement reason: $reason")
        }
        
        threatEventData.message?.let { message ->
            Log.i(TAG, "Conditional enforcement details: ${message.take(100)}")
        }
        
        return if (shouldEnforce) {
            Log.w(TAG, "Conditional enforcement requires action - user will be notified")
            ConditionalEnforcementResult.ENFORCE_WITH_NOTIFICATION
        } else {
            Log.i(TAG, "Conditional enforcement allows app to continue - event logged for review")
            ConditionalEnforcementResult.LOG_ONLY
        }
    }
    
    /**
     * Analyzes the threat event data to determine if enforcement should occur.
     * 
     * According to Appdome documentation, Conditional Enforcement should NOT
     * automatically terminate the app unless specific conditions are met.
     * 
     * @param threatEventData The threat event data to analyze
     * @return true if enforcement is required, false if event should only be logged
     */
    private fun analyzeEnforcementDecision(threatEventData: ThreatEventData): Boolean {
        // Check if failSafeEnforce explicitly indicates enforcement is required
        val failSafeEnforce = threatEventData.failSafeEnforce?.lowercase()
        
        // Only enforce if explicitly required by Appdome's conditional enforcement logic
        return when {
            failSafeEnforce == "true" || failSafeEnforce == "enforce" -> {
                Log.w(TAG, "Explicit enforcement required by failSafeEnforce: $failSafeEnforce")
                true
            }
            failSafeEnforce == "false" || failSafeEnforce == "allow" -> {
                Log.i(TAG, "Conditional enforcement allows app to continue: $failSafeEnforce")
                false
            }
            // If reasonCode indicates critical threat, consider enforcement
            threatEventData.reasonCode?.contains("CRITICAL", ignoreCase = true) == true -> {
                Log.w(TAG, "Critical threat detected in reasonCode: ${threatEventData.reasonCode}")
                true
            }
            else -> {
                // Default behavior for Conditional Enforcement: DO NOT enforce
                // This prevents unintended app termination as per Appdome design
                Log.i(TAG, "No explicit enforcement required - conditional enforcement allows continuation")
                false
            }
        }
    }
}

/**
 * Result of conditional enforcement analysis
 */
enum class ConditionalEnforcementResult {
    /** Event should only be logged, no user interruption */
    LOG_ONLY,
    /** Enforcement is required, user should be notified */
    ENFORCE_WITH_NOTIFICATION
}