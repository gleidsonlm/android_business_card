package com.gleidsonlm.businesscard.security

import android.util.Log
import com.gleidsonlm.businesscard.model.ThreatEventData
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Handler for KernelSUDetected threat events from Appdome.
 * 
 * Detects KernelSU installations which provide root access through kernel modifications.
 * Reference: https://www.appdome.com/how-to/mobile-malware-prevention/android-malware-detection/detect-kernelsu-in-android-apps/
 */
@Singleton
class KernelSUDetectedHandler @Inject constructor() {

    companion object {
        private const val TAG = "KernelSUDetected"
    }

    fun handleKernelSUDetectedEvent(threatEventData: ThreatEventData) {
        val sanitizedData = "id=${threatEventData.deviceID}, type=${threatEventData.threatCode}"
        Log.w(TAG, "KernelSU Installation Detected: $sanitizedData")
    }
}