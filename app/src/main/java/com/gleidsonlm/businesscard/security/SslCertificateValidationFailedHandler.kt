package com.gleidsonlm.businesscard.security

import android.util.Log
import com.gleidsonlm.businesscard.model.ThreatEventData
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Handler for SslCertificateValidationFailed threat events from Appdome.
 * 
 * Detects SSL certificate validation failures which may indicate man-in-the-middle attacks.
 * Reference: https://www.appdome.com/how-to/mobile-app-security/man-in-the-middle-attack-prevention/use-android-tls-ssl-certificate-validation-prevent-mitm-attacks/
 */
@Singleton
class SslCertificateValidationFailedHandler @Inject constructor() {

    companion object {
        private const val TAG = "SslCertificateValidationFailed"
    }

    fun handleSslCertificateValidationFailedEvent(threatEventData: ThreatEventData) {
        val sanitizedData = "id=${threatEventData.deviceID}, type=${threatEventData.threatCode}"
        Log.w(TAG, "SSL Certificate Validation Failed: $sanitizedData")
    }
}