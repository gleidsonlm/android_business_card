package com.gleidsonlm.businesscard.security

import android.util.Log
import com.gleidsonlm.businesscard.model.ThreatEventData
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Handler for RunningInVirtualSpace threat events from Appdome.
 * 
 * Detects virtual devices and environments which may indicate fraud or testing.
 * Reference: https://www.appdome.com/how-to/mobile-fraud-prevention-detection/know-your-customer-checks/detect-virtual-devices-environments-protect-android-ios-apps/
 */
@Singleton
class RunningInVirtualSpaceHandler @Inject constructor() {

    companion object {
        private const val TAG = "RunningInVirtualSpace"
    }

    fun handleRunningInVirtualSpaceEvent(threatEventData: ThreatEventData) {
        val sanitizedData = "id=${threatEventData.deviceID}, type=${threatEventData.threatCode}"
        Log.w(TAG, "Running In Virtual Space: $sanitizedData")
    }
}