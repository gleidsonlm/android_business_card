package com.gleidsonlm.businesscard.security

import android.util.Log
import com.gleidsonlm.businesscard.model.ThreatEventData
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Handler for SimStateInfo threat events from Appdome.
 * 
 * Detects SIM card swapping which may indicate account takeover attempts.
 * Reference: https://www.appdome.com/how-to/account-takeover-prevention/account-protection/detect-sim-card-swapping-in-android-ios/
 */
@Singleton
class SimStateInfoHandler @Inject constructor() {

    companion object {
        private const val TAG = "SimStateInfo"
    }

    /**
     * Handles the SimStateInfo threat event.
     * 
     * This event is triggered when SIM card state changes are detected that could indicate
     * a SIM swap attack where an attacker transfers the victim's phone number to their SIM card.
     * 
     * @param threatEventData The comprehensive threat event data from Appdome
     */
    fun handleSimStateInfoEvent(threatEventData: ThreatEventData) {
        val sanitizedData = "id=${threatEventData.deviceID}, type=${threatEventData.threatCode}"
        Log.w(TAG, "SIM State Change Detected: $sanitizedData")
        
        // Log carrier information if available for additional context
        threatEventData.carrierName?.let { carrier ->
            Log.w(TAG, "Carrier change detected: $carrier")
        }
        
        threatEventData.carrierPlmn?.let { plmn ->
            Log.w(TAG, "PLMN change detected: $plmn")
        }
        
        // Note: SIM swap detection provides critical security intelligence.
        // Consider implementing additional user verification flows when this event occurs.
    }
}