package com.gleidsonlm.businesscard.security

import android.util.Log
import com.gleidsonlm.businesscard.model.ThreatEventData
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Handler for GeoFencingUnauthorizedLocation threat events from Appdome.
 * 
 * Detects and restricts access if the device is outside authorized geofenced locations.
 * Reference: https://www.appdome.com/how-to/mobile-fraud-prevention-detection/geo-compliance/implement-geo-fencing-in-android-apps-using-ai/
 */
@Singleton
class GeoFencingUnauthorizedLocationHandler @Inject constructor() {

    companion object {
        private const val TAG = "GeoFencingUnauthorized"
    }

    fun handleGeoFencingUnauthorizedLocationEvent(threatEventData: ThreatEventData) {
        val sanitizedData = "id=${threatEventData.deviceID}, type=${threatEventData.threatCode}, location=${threatEventData.locationLat},${threatEventData.locationLong}, state=${threatEventData.locationState}"
        Log.w(TAG, "Geo-Fencing Unauthorized Location Detected: $sanitizedData")
    }
}