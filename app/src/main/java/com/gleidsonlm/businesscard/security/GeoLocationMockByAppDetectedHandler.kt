package com.gleidsonlm.businesscard.security

import android.util.Log
import com.gleidsonlm.businesscard.model.ThreatEventData
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Handler for GeoLocationMockByAppDetected threat events from Appdome.
 * 
 * Detects if a mock location app is installed or used.
 * Reference: https://www.appdome.com/how-to/mobile-fraud-prevention-detection/geo-compliance/detect-fake-gps-app-on-android/
 */
@Singleton
class GeoLocationMockByAppDetectedHandler @Inject constructor() {

    companion object {
        private const val TAG = "GeoLocationMockByApp"
    }

    fun handleGeoLocationMockByAppDetectedEvent(threatEventData: ThreatEventData) {
        val sanitizedData = "id=${threatEventData.deviceID}, type=${threatEventData.threatCode}, location=${threatEventData.locationLat},${threatEventData.locationLong}"
        Log.w(TAG, "Geo-Location Mock By App Detected: $sanitizedData")
    }
}