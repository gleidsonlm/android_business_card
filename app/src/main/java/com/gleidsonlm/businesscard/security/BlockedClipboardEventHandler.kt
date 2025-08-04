package com.gleidsonlm.businesscard.security

import android.util.Log
import com.gleidsonlm.businesscard.model.ThreatEventData
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Handler for BlockedClipboardEvent threat events from Appdome.
 * 
 * Blocks clipboard actions and notifies when potential copy/paste attacks are detected.
 * Reference: https://www.appdome.com/how-to/account-takeover-prevention/account-protection/protect-android-ios-apps-from-copypaste-attacks/
 */
@Singleton
class BlockedClipboardEventHandler @Inject constructor() {

    companion object {
        private const val TAG = "BlockedClipboardEvent"
    }

    /**
     * Handles the BlockedClipboardEvent threat event.
     * 
     * This event is triggered when clipboard access is blocked to prevent copy/paste attacks
     * where malicious apps attempt to steal sensitive information from the clipboard or
     * inject malicious content into the app.
     * 
     * @param threatEventData The comprehensive threat event data from Appdome
     */
    fun handleBlockedClipboardEventEvent(threatEventData: ThreatEventData) {
        val sanitizedData = "id=${threatEventData.deviceID}, type=${threatEventData.threatCode}"
        Log.w(TAG, "Clipboard Access Blocked: $sanitizedData")
        
        // Log the protective action taken
        Log.i(TAG, "Clipboard protection activated - potential copy/paste attack prevented")
        
        threatEventData.message?.let { message ->
            Log.w(TAG, "Clipboard threat context: ${message.take(100)}")
        }
        
        // Note: This event indicates that Appdome successfully blocked a potentially malicious
        // clipboard operation, protecting sensitive app data from extraction or modification.
    }
}