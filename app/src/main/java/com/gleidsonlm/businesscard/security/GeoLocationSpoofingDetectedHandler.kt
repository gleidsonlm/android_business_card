package com.gleidsonlm.businesscard.security

import android.util.Log
import com.gleidsonlm.businesscard.model.ThreatEventData
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Handler for GeoLocationSpoofingDetected threat events from Appdome.
 * 
 * Detects if the device's location is being spoofed or faked.
 * Reference: https://www.appdome.com/how-to/mobile-fraud-prevention-detection/geo-compliance/detect-fake-location-in-android-apps/
 */
@Singleton
class GeoLocationSpoofingDetectedHandler @Inject constructor() {

    companion object {
        private const val TAG = "GeoLocationSpoofing"
    }

    fun handleGeoLocationSpoofingDetectedEvent(threatEventData: ThreatEventData) {
        val sanitizedData = "id=${threatEventData.deviceID}, type=${threatEventData.threatCode}, location=${threatEventData.locationLat},${threatEventData.locationLong}"
        Log.w(TAG, "Geo-Location Spoofing Detected: $sanitizedData")
    }
}