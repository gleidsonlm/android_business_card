package com.gleidsonlm.businesscard.security

import android.util.Log
import com.gleidsonlm.businesscard.model.ThreatEventData
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Handler for SslInvalidMinDigest threat events from Appdome.
 * 
 * Detects insecure digest algorithms and enforces SHA-256 or stronger.
 * Reference: https://www.appdome.com/how-to/mobile-app-security/man-in-the-middle-attack-prevention/enforce-sha-256-digest-in-android-ios-apps/
 */
@Singleton
class SslInvalidMinDigestHandler @Inject constructor() {

    companion object {
        private const val TAG = "SslInvalidMinDigest"
    }

    fun handleSslInvalidMinDigestEvent(threatEventData: ThreatEventData) {
        val sanitizedData = "id=${threatEventData.deviceID}, type=${threatEventData.threatCode}"
        Log.w(TAG, "Invalid minimum digest strength detected: $sanitizedData")
    }
}