package com.gleidsonlm.businesscard.security

import android.util.Log
import com.gleidsonlm.businesscard.model.ThreatEventData
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Handler for AbusiveAccessibilityServiceDetected threat events from Appdome.
 * 
 * Detects abusive accessibility services that may be used by mobile remote access trojans.
 * Reference: https://www.appdome.com/how-to/account-takeover-prevention/android-and-ios-trojans/detect-mobile-remote-access-trojan/
 */
@Singleton
class AbusiveAccessibilityServiceDetectedHandler @Inject constructor() {

    companion object {
        private const val TAG = "AbusiveAccessibilityServiceDetected"
    }

    /**
     * Handles the AbusiveAccessibilityServiceDetected threat event.
     * 
     * This event is triggered when abusive accessibility services are detected that may be
     * used by mobile remote access trojans (RATs) to gain unauthorized control over the device,
     * steal sensitive information, or perform malicious actions without user consent.
     * 
     * @param threatEventData The comprehensive threat event data from Appdome
     */
    fun handleAbusiveAccessibilityServiceDetectedEvent(threatEventData: ThreatEventData) {
        val sanitizedData = "id=${threatEventData.deviceID}, type=${threatEventData.threatCode}"
        Log.e(TAG, "Abusive Accessibility Service Detected: $sanitizedData")
        
        // Log RAT detection context
        Log.e(TAG, "Potential Remote Access Trojan (RAT) activity detected")
        
        threatEventData.message?.let { message ->
            Log.e(TAG, "Abusive accessibility service details: ${message.take(100)}")
        }
        
        threatEventData.threatCode?.let { code ->
            Log.e(TAG, "RAT signature: $code")
        }
        
        // Note: Abusive accessibility services are a primary vector for RATs to gain
        // persistent device access and perform unauthorized actions.
    }
}