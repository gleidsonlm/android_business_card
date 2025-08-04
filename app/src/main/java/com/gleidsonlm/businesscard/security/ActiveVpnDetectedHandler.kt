package com.gleidsonlm.businesscard.security

import android.util.Log
import com.gleidsonlm.businesscard.model.ThreatEventData
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Handler for ActiveVpnDetected threat events from Appdome.
 * 
 * Detects if the device is using an active VPN connection.
 * Reference: https://www.appdome.com/how-to/mobile-fraud-prevention-detection/geo-compliance/vpn-detection/
 */
@Singleton
class ActiveVpnDetectedHandler @Inject constructor() {

    companion object {
        private const val TAG = "ActiveVpnDetected"
    }

    fun handleActiveVpnDetectedEvent(threatEventData: ThreatEventData) {
        val sanitizedData = "id=${threatEventData.deviceID}, type=${threatEventData.threatCode}"
        Log.w(TAG, "Active VPN Detected: $sanitizedData")
    }
}