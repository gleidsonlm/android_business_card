package com.gleidsonlm.businesscard.security

import android.util.Log
import com.gleidsonlm.businesscard.model.ThreatEventData
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Handler for SslIntegrityCheckFail threat events from Appdome.
 * 
 * Detects SSL integrity check failures which may indicate SSL pinning bypass attempts.
 * Reference: https://www.appdome.com/how-to/mobile-malware-prevention/android-malware-detection/learn-3-simple-steps-to-tries-to-block-ssl-pinning-attempts-to-overcome-ssl-pinning-by-using-dynamic-instrumentation-toolkits-such-as-frida/
 */
@Singleton
class SslIntegrityCheckFailHandler @Inject constructor() {

    companion object {
        private const val TAG = "SslIntegrityCheckFail"
    }

    fun handleSslIntegrityCheckFailEvent(threatEventData: ThreatEventData) {
        val sanitizedData = "id=${threatEventData.deviceID}, type=${threatEventData.threatCode}"
        Log.w(TAG, "SSL Integrity Check Failed: $sanitizedData")
    }
}