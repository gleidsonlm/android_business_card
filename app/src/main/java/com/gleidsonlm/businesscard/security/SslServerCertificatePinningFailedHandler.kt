package com.gleidsonlm.businesscard.security

import android.util.Log
import com.gleidsonlm.businesscard.model.ThreatEventData
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Handler for SslServerCertificatePinningFailed threat events from Appdome.
 * 
 * Detects SSL/TLS certificate pinning failures indicating potential MITM attacks.
 * Reference: https://www.appdome.com/how-to/mobile-app-security/man-in-the-middle-attack-prevention/use-secure-certificate-pinning-in-android-ios-apps/
 */
@Singleton
class SslServerCertificatePinningFailedHandler @Inject constructor() {

    companion object {
        private const val TAG = "SslServerCertificatePinningFailed"
    }

    fun handleSslServerCertificatePinningFailedEvent(threatEventData: ThreatEventData) {
        val sanitizedData = "id=${threatEventData.deviceID}, type=${threatEventData.threatCode}"
        Log.w(TAG, "SSL Certificate Pinning Failed: $sanitizedData")
    }
}