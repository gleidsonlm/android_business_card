package com.gleidsonlm.businesscard.security

import android.util.Log
import com.gleidsonlm.businesscard.model.ThreatEventData
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Handler for BlockedKeyboardEvent threat events from Appdome.
 * 
 * Detects and blocks unauthorized keyboard events to prevent keylogging malware.
 * Reference: https://www.appdome.com/how-to/account-takeover-prevention/account-protection/protect-android-ios-apps-from-keylogging-malware/
 */
@Singleton
class BlockedKeyboardEventHandler @Inject constructor() {

    companion object {
        private const val TAG = "BlockedKeyboardEvent"
    }

    fun handleBlockedKeyboardEventEvent(threatEventData: ThreatEventData) {
        val sanitizedData = "id=${threatEventData.deviceID}, type=${threatEventData.threatCode}"
        Log.w(TAG, "Keyboard Event Blocked: $sanitizedData")
    }
}