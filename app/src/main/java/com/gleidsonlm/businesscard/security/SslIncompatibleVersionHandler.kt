package com.gleidsonlm.businesscard.security

import android.util.Log
import com.gleidsonlm.businesscard.model.ThreatEventData
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Handler for SslIncompatibleVersion threat events from Appdome.
 * 
 * Detects incompatible SSL/TLS versions which may indicate security vulnerabilities.
 * Reference: https://www.appdome.com/how-to/mobile-app-security/man-in-the-middle-attack-prevention/enforce-minimum-tls-version-prevent-tlsssl-attacks-in-android-ios-apps/
 */
@Singleton
class SslIncompatibleVersionHandler @Inject constructor() {

    companion object {
        private const val TAG = "SslIncompatibleVersion"
    }

    fun handleSslIncompatibleVersionEvent(threatEventData: ThreatEventData) {
        val sanitizedData = "id=${threatEventData.deviceID}, type=${threatEventData.threatCode}"
        Log.w(TAG, "SSL Incompatible Version Detected: $sanitizedData")
    }
}