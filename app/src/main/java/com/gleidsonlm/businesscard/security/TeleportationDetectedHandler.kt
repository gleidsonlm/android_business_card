package com.gleidsonlm.businesscard.security

import android.util.Log
import com.gleidsonlm.businesscard.model.ThreatEventData
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Handler for TeleportationDetected threat events from Appdome.
 * 
 * Detects if device location changes suddenly or unrealistically (teleportation).
 * Reference: https://www.appdome.com/how-to/mobile-fraud-prevention-detection/geo-compliance/detect-teleportation/
 */
@Singleton
class TeleportationDetectedHandler @Inject constructor() {

    companion object {
        private const val TAG = "TeleportationDetected"
    }

    fun handleTeleportationDetectedEvent(threatEventData: ThreatEventData) {
        val sanitizedData = "id=${threatEventData.deviceID}, type=${threatEventData.threatCode}, location=${threatEventData.locationLat},${threatEventData.locationLong}"
        Log.w(TAG, "Teleportation Detected: $sanitizedData")
    }
}