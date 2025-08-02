package com.gleidsonlm.businesscard.security

import android.util.Log
import com.gleidsonlm.businesscard.model.ThreatEventData
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Handler for SslInvalidCertificateChain threat events from Appdome.
 * 
 * Detects invalid TLS certificate chains to prevent MITM attacks.
 * Reference: https://www.appdome.com/how-to/mobile-app-security/man-in-the-middle-attack-prevention/enforce-tls-certificate-roles-prevent-mitm-attacks-in-ios-apps/
 */
@Singleton
class SslInvalidCertificateChainHandler @Inject constructor() {

    companion object {
        private const val TAG = "SslInvalidCertificateChain"
    }

    fun handleSslInvalidCertificateChainEvent(threatEventData: ThreatEventData) {
        val sanitizedData = "id=${threatEventData.deviceID}, type=${threatEventData.threatCode}"
        Log.w(TAG, "Invalid SSL certificate chain detected: $sanitizedData")
    }
}