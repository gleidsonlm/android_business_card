package com.gleidsonlm.businesscard.security

import android.util.Log
import com.gleidsonlm.businesscard.model.ThreatEventData
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Handler for ClickBotDetectedVirtualFinger threat events from Appdome.
 * 
 * Detects virtual finger or click bot activity indicating automated malicious interactions.
 * Reference: https://www.appdome.com/how-to/account-takeover-prevention/social-engineering-prevention/detect-malicious-remote-clicks-in-android-apps-using-ai/
 */
@Singleton
class ClickBotDetectedVirtualFingerHandler @Inject constructor() {

    companion object {
        private const val TAG = "ClickBotDetectedVirtualFinger"
    }

    fun handleClickBotDetectedVirtualFingerEvent(threatEventData: ThreatEventData) {
        val sanitizedData = "id=${threatEventData.deviceID}, type=${threatEventData.threatCode}"
        Log.w(TAG, "Click Bot Virtual Finger Detected: $sanitizedData")
    }
}