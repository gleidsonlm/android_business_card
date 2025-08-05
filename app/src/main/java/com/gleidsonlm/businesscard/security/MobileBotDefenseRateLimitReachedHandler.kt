package com.gleidsonlm.businesscard.security

import android.util.Log
import com.gleidsonlm.businesscard.model.ThreatEventData
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Handler for MobileBotDefenseRateLimitReached threat events from Appdome.
 * 
 * Triggered when rate limits are reached as part of mobile bot defense.
 * This event indicates that the application has detected and enforced rate
 * limiting as a protective measure against automated bot attacks.
 * 
 * Reference: https://www.appdome.com/how-to/mobile-bot-detection/api-bot-defense/rate-limit-connections-anti-bot-defense/
 */
@Singleton
class MobileBotDefenseRateLimitReachedHandler @Inject constructor() {

    companion object {
        private const val TAG = "MobileBotDefenseRateLimit"
    }

    /**
     * Handles the MobileBotDefenseRateLimitReached threat event.
     * 
     * This method logs the rate limit enforcement and provides information about
     * the bot defense system's response to potential automated threats.
     * 
     * @param threatEventData The comprehensive threat event data from Appdome
     */
    fun handleRateLimitReachedEvent(threatEventData: ThreatEventData) {
        // Sanitize sensitive data for logging
        val sanitizedData = buildString {
            append("deviceModel=${threatEventData.deviceModel}")
            append(", threatCode=${threatEventData.threatCode}")
            append(", timestamp=${threatEventData.timeStamp}")
            append(", reasonCode=${threatEventData.reasonCode}")
        }
        
        Log.w(TAG, "Mobile Bot Defense Rate Limit Reached: $sanitizedData")
        
        // Additional analysis based on threat metadata
        analyzeRateLimitContext(threatEventData)
    }
    
    /**
     * Analyzes the context of the rate limit event to provide additional insights.
     */
    private fun analyzeRateLimitContext(threatEventData: ThreatEventData) {
        val contextInfo = mutableListOf<String>()
        
        // Analyze threat code for additional context
        threatEventData.threatCode?.let { code ->
            when {
                code.contains("RAPID_REQUEST", ignoreCase = true) -> {
                    contextInfo.add("Rapid request pattern detected")
                }
                code.contains("BURST_ACTIVITY", ignoreCase = true) -> {
                    contextInfo.add("Burst activity pattern detected")
                }
                code.contains("AUTOMATED_BEHAVIOR", ignoreCase = true) -> {
                    contextInfo.add("Automated behavior pattern detected")
                }
            }
        }
        
        // Analyze reason code for enforcement context
        threatEventData.reasonCode?.let { reason ->
            when {
                reason.contains("API_RATE_EXCEEDED", ignoreCase = true) -> {
                    contextInfo.add("API rate limit exceeded")
                }
                reason.contains("CONNECTION_LIMIT", ignoreCase = true) -> {
                    contextInfo.add("Connection rate limit enforced")
                }
                reason.contains("REQUEST_FREQUENCY", ignoreCase = true) -> {
                    contextInfo.add("Request frequency threshold exceeded")
                }
            }
        }
        
        if (contextInfo.isNotEmpty()) {
            Log.i(TAG, "Rate limit context: ${contextInfo.joinToString(", ")}")
        }
        
        // Log device characteristics for bot pattern analysis
        logDeviceCharacteristics(threatEventData)
    }
    
    /**
     * Logs relevant device characteristics for bot pattern analysis.
     */
    private fun logDeviceCharacteristics(threatEventData: ThreatEventData) {
        val deviceInfo = mutableListOf<String>()
        
        threatEventData.deviceManufacturer?.let { manufacturer ->
            deviceInfo.add("manufacturer=$manufacturer")
        }
        
        threatEventData.osVersion?.let { version ->
            deviceInfo.add("osVersion=$version")
        }
        
        threatEventData.buildHost?.let { buildHost ->
            if (buildHost.contains("build", ignoreCase = true) || 
                buildHost.contains("test", ignoreCase = true)) {
                deviceInfo.add("developmentBuild=true")
            }
        }
        
        if (deviceInfo.isNotEmpty()) {
            Log.d(TAG, "Device characteristics: ${deviceInfo.joinToString(", ")}")
        }
    }
}