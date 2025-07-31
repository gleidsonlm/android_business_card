package com.gleidsonlm.businesscard.security

import android.util.Log
import com.gleidsonlm.businesscard.model.ThreatEventData
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Handler for ActivePhoneCallDetected threat events from Appdome.
 * 
 * Detects active phone calls during sensitive actions to prevent social engineering attacks.
 * Reference: https://www.appdome.com/how-to/account-takeover-prevention/social-engineering-prevention/use-anti-vishing/
 */
@Singleton
class ActivePhoneCallDetectedHandler @Inject constructor() {

    companion object {
        private const val TAG = "ActivePhoneCallDetected"
    }

    fun handleActivePhoneCallDetectedEvent(threatEventData: ThreatEventData) {
        val sanitizedData = "id=${threatEventData.deviceID}, type=${threatEventData.threatCode}"
        Log.w(TAG, "Active Phone Call Detected During Sensitive Action: $sanitizedData")
    }
}