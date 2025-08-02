package com.gleidsonlm.businesscard.security

import android.util.Log
import com.gleidsonlm.businesscard.model.ThreatEventData
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Handler for SslIncompatibleCipher threat events from Appdome.
 * 
 * Detects incompatible SSL/TLS cipher suites to enforce secure connections.
 * Reference: https://www.appdome.com/how-to/mobile-app-security/man-in-the-middle-attack-prevention/enforce-ssl-tls-cipher-suites-in-mobile-apps/
 */
@Singleton
class SslIncompatibleCipherHandler @Inject constructor() {

    companion object {
        private const val TAG = "SslIncompatibleCipher"
    }

    fun handleSslIncompatibleCipherEvent(threatEventData: ThreatEventData) {
        val sanitizedData = "id=${threatEventData.deviceID}, type=${threatEventData.threatCode}"
        Log.w(TAG, "Incompatible SSL/TLS cipher detected: $sanitizedData")
    }
}