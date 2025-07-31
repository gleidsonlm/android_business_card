package com.gleidsonlm.businesscard.security

import android.util.Log
import com.gleidsonlm.businesscard.model.ThreatEventData
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Handler for ClickBotDetectedByPermissions threat events from Appdome.
 * 
 * Detects mobile auto-clicker applications through permission analysis.
 * Reference: https://www.appdome.com/how-to/mobile-fraud-prevention-detection/know-your-customer-checks/detect-mobile-auto-clicker-permissions-in-android-apps/
 */
@Singleton
class ClickBotDetectedByPermissionsHandler @Inject constructor() {

    companion object {
        private const val TAG = "ClickBotDetectedByPermissions"
    }

    fun handleClickBotDetectedByPermissionsEvent(threatEventData: ThreatEventData) {
        val sanitizedData = "id=${threatEventData.deviceID}, type=${threatEventData.threatCode}"
        Log.w(TAG, "Click Bot Detected By Permissions: $sanitizedData")
    }
}