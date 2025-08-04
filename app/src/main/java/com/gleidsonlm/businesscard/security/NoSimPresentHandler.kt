package com.gleidsonlm.businesscard.security

import android.util.Log
import com.gleidsonlm.businesscard.model.ThreatEventData
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Handler for NoSimPresent threat events from Appdome.
 * 
 * Detects if the device does not have a SIM card present.
 * Reference: https://www.appdome.com/how-to/mobile-fraud-prevention-detection/geo-compliance/check-for-missing-sim-cards-on-android-ios-devices/
 */
@Singleton
class NoSimPresentHandler @Inject constructor() {

    companion object {
        private const val TAG = "NoSimPresent"
    }

    fun handleNoSimPresentEvent(threatEventData: ThreatEventData) {
        val sanitizedData = "id=${threatEventData.deviceID}, type=${threatEventData.threatCode}, carrier=${threatEventData.carrierName}"
        Log.w(TAG, "No SIM Present Detected: $sanitizedData")
    }
}