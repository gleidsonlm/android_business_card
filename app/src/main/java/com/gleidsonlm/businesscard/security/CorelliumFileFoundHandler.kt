package com.gleidsonlm.businesscard.security

import android.util.Log
import com.gleidsonlm.businesscard.model.ThreatEventData
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Handler for CorelliumFileFound threat events from Appdome.
 * 
 * Detects Corellium ARM-in-ARM virtual devices which may indicate testing or fraud.
 * Reference: https://www.appdome.com/how-to/mobile-fraud-prevention-detection/know-your-customer-checks/block-arm-in-arm-virtual-devices/
 */
@Singleton
class CorelliumFileFoundHandler @Inject constructor() {

    companion object {
        private const val TAG = "CorelliumFileFound"
    }

    fun handleCorelliumFileFoundEvent(threatEventData: ThreatEventData) {
        val sanitizedData = "id=${threatEventData.deviceID}, type=${threatEventData.threatCode}"
        Log.w(TAG, "Corellium File Found: $sanitizedData")
    }
}