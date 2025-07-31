package com.gleidsonlm.businesscard.security

import android.util.Log
import com.gleidsonlm.businesscard.model.ThreatEventData
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Handler for SpeedHackDetected threat events from Appdome.
 * 
 * Detects speed hacking applications which may indicate cheating or fraud.
 * Reference: https://www.appdome.com/how-to/mobile-cheat-prevention/mobile-app-modding-detection/block-speed-hacking-speed-hacks-in-android-apps/
 */
@Singleton
class SpeedHackDetectedHandler @Inject constructor() {

    companion object {
        private const val TAG = "SpeedHackDetected"
    }

    fun handleSpeedHackDetectedEvent(threatEventData: ThreatEventData) {
        val sanitizedData = "id=${threatEventData.deviceID}, type=${threatEventData.threatCode}"
        Log.w(TAG, "Speed Hack Detected: $sanitizedData")
    }
}