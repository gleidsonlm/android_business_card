package com.gleidsonlm.businesscard.security

import android.util.Log
import com.gleidsonlm.businesscard.model.ThreatEventData
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Handler for ClickBotDetected threat events from Appdome.
 * 
 * Detects mobile auto-clicker applications which may indicate fraudulent activity.
 * Reference: https://www.appdome.com/how-to/mobile-fraud-prevention-detection/know-your-customer-checks/detect-mobile-auto-clicker-in-android-apps/
 */
@Singleton
class ClickBotDetectedHandler @Inject constructor() {

    companion object {
        private const val TAG = "ClickBotDetected"
    }

    fun handleClickBotDetectedEvent(threatEventData: ThreatEventData) {
        val sanitizedData = "id=${threatEventData.deviceID}, type=${threatEventData.threatCode}"
        Log.w(TAG, "Click Bot Detected: $sanitizedData")
    }
}