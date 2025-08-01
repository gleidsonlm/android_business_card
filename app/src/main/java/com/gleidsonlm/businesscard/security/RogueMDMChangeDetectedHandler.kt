package com.gleidsonlm.businesscard.security

import android.util.Log
import com.gleidsonlm.businesscard.model.ThreatEventData
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Handler for RogueMDMChangeDetected threat events from Appdome.
 * 
 * Detects rogue Mobile Device Management (MDM) changes indicating potential device admin malware.
 * Reference: https://www.appdome.com/how-to/account-takeover-prevention/account-protection/android-device-admin-malware/
 */
@Singleton
class RogueMDMChangeDetectedHandler @Inject constructor() {

    companion object {
        private const val TAG = "RogueMDMChangeDetected"
    }

    fun handleRogueMDMChangeDetectedEvent(threatEventData: ThreatEventData) {
        val sanitizedData = "id=${threatEventData.deviceID}, type=${threatEventData.threatCode}"
        Log.w(TAG, "Rogue MDM Change Detected: $sanitizedData")
    }
}