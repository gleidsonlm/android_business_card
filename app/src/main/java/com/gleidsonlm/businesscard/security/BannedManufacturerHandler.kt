package com.gleidsonlm.businesscard.security

import android.util.Log
import com.gleidsonlm.businesscard.model.ThreatEventData
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Handler for BannedManufacturer threat events from Appdome.
 * 
 * Detects banned device manufacturers to ensure security compliance.
 * Reference: https://www.appdome.com/how-to/mobile-app-security/jailbreak-root-detection/detect-banned-devices-in-android-apps/
 */
@Singleton
class BannedManufacturerHandler @Inject constructor() {

    companion object {
        private const val TAG = "BannedManufacturer"
    }

    fun handleBannedManufacturerEvent(threatEventData: ThreatEventData) {
        val sanitizedData = "id=${threatEventData.deviceID}, manufacturer=${threatEventData.deviceManufacturer}, brand=${threatEventData.deviceBrand}"
        Log.w(TAG, "Banned device manufacturer detected: $sanitizedData")
    }
}