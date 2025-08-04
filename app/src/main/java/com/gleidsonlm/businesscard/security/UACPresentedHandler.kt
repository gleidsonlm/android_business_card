package com.gleidsonlm.businesscard.security

import android.util.Log
import com.gleidsonlm.businesscard.model.ThreatEventData
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Handler for UACPresented threat events from Appdome.
 * 
 * Presents user accessibility service consent event to provide transparency about accessibility permissions.
 * Reference: https://www.appdome.com/how-to/account-takeover-prevention/android-and-ios-trojans/user-accessibility-service-consent/
 */
@Singleton
class UACPresentedHandler @Inject constructor() {

    companion object {
        private const val TAG = "UACPresented"
    }

    /**
     * Handles the UACPresented threat event.
     * 
     * This event is triggered when User Accessibility Consent (UAC) is presented to the user,
     * providing transparency about accessibility service permissions and ensuring users are
     * aware of potentially risky accessibility permissions being requested or used.
     * 
     * @param threatEventData The comprehensive threat event data from Appdome
     */
    fun handleUACPresentedEvent(threatEventData: ThreatEventData) {
        val sanitizedData = "id=${threatEventData.deviceID}, type=${threatEventData.threatCode}"
        Log.i(TAG, "User Accessibility Consent Presented: $sanitizedData")
        
        // Log user consent context
        Log.i(TAG, "Accessibility permission transparency event triggered")
        
        threatEventData.message?.let { message ->
            Log.i(TAG, "UAC presentation details: ${message.take(100)}")
        }
        
        threatEventData.reasonCode?.let { reason ->
            Log.i(TAG, "UAC trigger reason: $reason")
        }
        
        // Note: This event enhances user awareness about accessibility permissions
        // which can be abused by malicious apps for overlay attacks or data theft.
    }
}