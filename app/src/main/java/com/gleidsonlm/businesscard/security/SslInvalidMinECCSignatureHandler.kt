package com.gleidsonlm.businesscard.security

import android.util.Log
import com.gleidsonlm.businesscard.model.ThreatEventData
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Handler for SslInvalidMinECCSignature threat events from Appdome.
 * 
 * Detects ECC signatures that do not meet the required strength.
 * Reference: https://www.appdome.com/how-to/mobile-app-security/man-in-the-middle-attack-prevention/enforce-strong-ecc-signature-in-android-apps/
 */
@Singleton
class SslInvalidMinECCSignatureHandler @Inject constructor() {

    companion object {
        private const val TAG = "SslInvalidMinECCSignature"
    }

    fun handleSslInvalidMinECCSignatureEvent(threatEventData: ThreatEventData) {
        val sanitizedData = "id=${threatEventData.deviceID}, type=${threatEventData.threatCode}"
        Log.w(TAG, "Invalid minimum ECC signature strength detected: $sanitizedData")
    }
}