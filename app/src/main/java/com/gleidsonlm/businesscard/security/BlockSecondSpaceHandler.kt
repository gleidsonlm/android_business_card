package com.gleidsonlm.businesscard.security

import android.util.Log
import com.gleidsonlm.businesscard.model.ThreatEventData
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Handler for BlockSecondSpace threat events from Appdome.
 * 
 * Detects secondary space applications which may indicate privacy bypass attempts.
 * Reference: https://www.appdome.com/how-to/mobile-fraud-prevention-detection/know-your-customer-checks/block-secondary-space-in-android-apps/
 */
@Singleton
class BlockSecondSpaceHandler @Inject constructor() {

    companion object {
        private const val TAG = "BlockSecondSpace"
    }

    fun handleBlockSecondSpaceEvent(threatEventData: ThreatEventData) {
        val sanitizedData = "id=${threatEventData.deviceID}, type=${threatEventData.threatCode}"
        Log.w(TAG, "Second Space Detected: $sanitizedData")
    }
}