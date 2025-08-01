package com.gleidsonlm.businesscard.security

import android.util.Log
import com.gleidsonlm.businesscard.model.ThreatEventData
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Handler for BlockedScreenCaptureEvent threat events from Appdome.
 * 
 * Detects and blocks screen capture attempts to prevent data exfiltration.
 * Reference: https://www.appdome.com/how-to/account-takeover-prevention/social-engineering-prevention/protect-android-apps-from-screen-sharing-malware/
 */
@Singleton
class BlockedScreenCaptureEventHandler @Inject constructor() {

    companion object {
        private const val TAG = "BlockedScreenCaptureEvent"
    }

    fun handleBlockedScreenCaptureEventEvent(threatEventData: ThreatEventData) {
        val sanitizedData = "id=${threatEventData.deviceID}, type=${threatEventData.threatCode}"
        Log.w(TAG, "Screen Capture Blocked: $sanitizedData")
    }
}