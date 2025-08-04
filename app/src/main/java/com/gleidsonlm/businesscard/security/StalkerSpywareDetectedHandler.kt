package com.gleidsonlm.businesscard.security

import android.util.Log
import com.gleidsonlm.businesscard.model.ThreatEventData
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Handler for StalkerSpywareDetected threat events from Appdome.
 * 
 * Detects stalker spyware that may be used for unauthorized surveillance or tracking.
 * Reference: https://www.appdome.com/how-to/account-takeover-prevention/android-and-ios-trojans/detect-stalker-spyware-in-android-apps/
 */
@Singleton
class StalkerSpywareDetectedHandler @Inject constructor() {

    companion object {
        private const val TAG = "StalkerSpywareDetected"
    }

    /**
     * Handles the StalkerSpywareDetected threat event.
     * 
     * This event is triggered when stalker spyware is detected on the device. Such spyware
     * is typically used for unauthorized surveillance, location tracking, communication monitoring,
     * or other invasive activities without the user's knowledge or consent.
     * 
     * @param threatEventData The comprehensive threat event data from Appdome
     */
    fun handleStalkerSpywareDetectedEvent(threatEventData: ThreatEventData) {
        val sanitizedData = "id=${threatEventData.deviceID}, type=${threatEventData.threatCode}"
        Log.e(TAG, "Stalker Spyware Detected: $sanitizedData")
        
        // Log privacy threat context
        Log.e(TAG, "Unauthorized surveillance software detected - privacy at risk")
        
        threatEventData.message?.let { message ->
            Log.e(TAG, "Stalker spyware details: ${message.take(100)}")
        }
        
        threatEventData.threatCode?.let { code ->
            Log.e(TAG, "Spyware signature: $code")
        }
        
        // Log location context if available (may be relevant for stalking scenarios)
        threatEventData.locationState?.let { location ->
            Log.w(TAG, "Location context available during spyware detection")
        }
        
        // Note: Stalker spyware represents a serious privacy threat and may indicate
        // domestic abuse, corporate espionage, or other serious security breaches.
    }
}