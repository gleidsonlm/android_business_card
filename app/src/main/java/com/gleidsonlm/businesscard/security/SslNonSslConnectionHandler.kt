package com.gleidsonlm.businesscard.security

import android.util.Log
import com.gleidsonlm.businesscard.model.ThreatEventData
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Handler for SslNonSslConnection threat events from Appdome.
 * 
 * Detects non-SSL connections which may indicate security vulnerabilities.
 * Reference: https://www.appdome.com/how-to/mobile-app-security/man-in-the-middle-attack-prevention/block-non-ssl-connections-in-android-apps/
 */
@Singleton
class SslNonSslConnectionHandler @Inject constructor() {

    companion object {
        private const val TAG = "SslNonSslConnection"
    }

    fun handleSslNonSslConnectionEvent(threatEventData: ThreatEventData) {
        val sanitizedData = "id=${threatEventData.deviceID}, type=${threatEventData.threatCode}"
        Log.w(TAG, "Non-SSL Connection Detected: $sanitizedData")
    }
}