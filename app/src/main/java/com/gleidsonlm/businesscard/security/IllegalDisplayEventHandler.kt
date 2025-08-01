package com.gleidsonlm.businesscard.security

import android.util.Log
import com.gleidsonlm.businesscard.model.ThreatEventData
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Handler for IllegalDisplayEvent threat events from Appdome.
 * 
 * Detects illegal or remote displays that may indicate remote desktop scams or unauthorized access.
 * Reference: https://www.appdome.com/how-to/account-takeover-prevention/social-engineering-prevention/prevent-remote-desktop-scams-in-android-apps/
 */
@Singleton
class IllegalDisplayEventHandler @Inject constructor() {

    companion object {
        private const val TAG = "IllegalDisplayEvent"
    }

    fun handleIllegalDisplayEventEvent(threatEventData: ThreatEventData) {
        val sanitizedData = "id=${threatEventData.deviceID}, type=${threatEventData.threatCode}"
        Log.w(TAG, "Illegal Display Event Detected: $sanitizedData")
    }
}