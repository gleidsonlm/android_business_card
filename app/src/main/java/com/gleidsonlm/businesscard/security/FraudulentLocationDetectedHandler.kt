package com.gleidsonlm.businesscard.security

import android.util.Log
import com.gleidsonlm.businesscard.model.ThreatEventData
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Handler for FraudulentLocationDetected threat events from Appdome.
 * 
 * Detects if the device location is desynchronized or fraudulent.
 * Reference: https://www.appdome.com/how-to/mobile-fraud-prevention-detection/geo-compliance/detect-geo-desync/
 */
@Singleton
class FraudulentLocationDetectedHandler @Inject constructor() {

    companion object {
        private const val TAG = "FraudulentLocation"
    }

    fun handleFraudulentLocationDetectedEvent(threatEventData: ThreatEventData) {
        val sanitizedData = "id=${threatEventData.deviceID}, type=${threatEventData.threatCode}, location=${threatEventData.locationLat},${threatEventData.locationLong}"
        Log.w(TAG, "Fraudulent Location Detected: $sanitizedData")
    }
}