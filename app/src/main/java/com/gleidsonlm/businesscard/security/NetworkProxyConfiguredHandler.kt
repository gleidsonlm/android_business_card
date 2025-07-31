package com.gleidsonlm.businesscard.security

import android.util.Log
import com.gleidsonlm.businesscard.model.ThreatEventData
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Handler for NetworkProxyConfigured threat events from Appdome.
 * 
 * Detects network proxy configurations which may indicate man-in-the-middle attacks.
 * Reference: https://www.appdome.com/how-to/mobile-app-security/man-in-the-middle-attack-prevention/detecting-deep-proxy/
 */
@Singleton
class NetworkProxyConfiguredHandler @Inject constructor() {

    companion object {
        private const val TAG = "NetworkProxyConfigured"
    }

    fun handleNetworkProxyConfiguredEvent(threatEventData: ThreatEventData) {
        val sanitizedData = "id=${threatEventData.deviceID}, type=${threatEventData.threatCode}"
        Log.w(TAG, "Network Proxy Configured: $sanitizedData")
    }
}