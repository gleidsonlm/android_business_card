package com.gleidsonlm.businesscard.security

import android.util.Log
import com.gleidsonlm.businesscard.model.ThreatEventData
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Handler for NotInstalledFromOfficialStore threat events from Appdome.
 * 
 * Detects applications not installed from official stores which may indicate sideloading.
 * Reference: https://www.appdome.com/how-to/mobile-fraud-prevention-detection/know-your-customer-checks/use-google-play-signature-validation-in-android-apps/
 */
@Singleton
class NotInstalledFromOfficialStoreHandler @Inject constructor() {

    companion object {
        private const val TAG = "NotInstalledFromOfficialStore"
    }

    fun handleNotInstalledFromOfficialStoreEvent(threatEventData: ThreatEventData) {
        val sanitizedData = "id=${threatEventData.deviceID}, type=${threatEventData.threatCode}"
        Log.w(TAG, "Not Installed From Official Store: $sanitizedData")
    }
}