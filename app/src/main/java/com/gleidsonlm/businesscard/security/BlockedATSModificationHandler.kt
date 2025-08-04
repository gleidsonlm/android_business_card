package com.gleidsonlm.businesscard.security

import android.util.Log
import com.gleidsonlm.businesscard.model.ThreatEventData
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Handler for BlockedATSModification threat events from Appdome.
 * 
 * Detects and blocks ATS (App Transport Security) modifications that may be used by banking trojans.
 * Reference: https://www.appdome.com/how-to/account-takeover-prevention/android-and-ios-trojans/detect-banking-trojan-apps-using-appdome/
 */
@Singleton
class BlockedATSModificationHandler @Inject constructor() {

    companion object {
        private const val TAG = "BlockedATSModification"
    }

    /**
     * Handles the BlockedATSModification threat event.
     * 
     * This event is triggered when attempts to modify App Transport Security (ATS) settings
     * are detected and blocked. Such modifications are often used by banking trojans and
     * other malware to intercept or manipulate secure network communications.
     * 
     * @param threatEventData The comprehensive threat event data from Appdome
     */
    fun handleBlockedATSModificationEvent(threatEventData: ThreatEventData) {
        val sanitizedData = "id=${threatEventData.deviceID}, type=${threatEventData.threatCode}"
        Log.e(TAG, "ATS Modification Blocked: $sanitizedData")
        
        // Log network security context
        Log.w(TAG, "Network security protection activated - ATS modification prevented")
        
        threatEventData.message?.let { message ->
            Log.e(TAG, "ATS modification attempt details: ${message.take(100)}")
        }
        
        threatEventData.threatCode?.let { code ->
            Log.e(TAG, "ATS threat signature: $code")
        }
        
        // Note: ATS modifications are commonly used by banking trojans to bypass
        // network security measures. Blocking these is critical for maintaining app security.
    }
}