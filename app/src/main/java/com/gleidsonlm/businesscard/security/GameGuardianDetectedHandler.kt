package com.gleidsonlm.businesscard.security

import android.util.Log
import com.gleidsonlm.businesscard.model.ThreatEventData
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Handler for GameGuardianDetected threat events from Appdome.
 * 
 * Detects GameGuardian cheating applications which may indicate fraud or gaming exploits.
 * Reference: https://www.appdome.com/how-to/mobile-cheat-prevention/mobile-app-modding-detection/block-gameguardian-cheating-apps-in-android-games/
 */
@Singleton
class GameGuardianDetectedHandler @Inject constructor() {

    companion object {
        private const val TAG = "GameGuardianDetected"
    }

    fun handleGameGuardianDetectedEvent(threatEventData: ThreatEventData) {
        val sanitizedData = "id=${threatEventData.deviceID}, type=${threatEventData.threatCode}"
        Log.w(TAG, "GameGuardian Detected: $sanitizedData")
    }
}