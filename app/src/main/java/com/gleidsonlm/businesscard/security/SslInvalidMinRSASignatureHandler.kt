package com.gleidsonlm.businesscard.security

import android.util.Log
import com.gleidsonlm.businesscard.model.ThreatEventData
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Handler for SslInvalidMinRSASignature threat events from Appdome.
 * 
 * Detects cases where minimum RSA signature strength is not met.
 * Reference: https://www.appdome.com/how-to/mobile-app-security/man-in-the-middle-attack-prevention/enforce-strong-rsa-signature-in-android-ios-apps/
 */
@Singleton
class SslInvalidMinRSASignatureHandler @Inject constructor() {

    companion object {
        private const val TAG = "SslInvalidMinRSASignature"
    }

    fun handleSslInvalidMinRSASignatureEvent(threatEventData: ThreatEventData) {
        val sanitizedData = "id=${threatEventData.deviceID}, type=${threatEventData.threatCode}"
        Log.w(TAG, "Invalid minimum RSA signature strength detected: $sanitizedData")
    }
}