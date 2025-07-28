package com.gleidsonlm.businesscard.security

import android.util.Log
import com.gleidsonlm.businesscard.model.ThreatEventData
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Handler for UnauthorizedAIAssistantDetected threat events from Appdome.
 * 
 * Detects unauthorized AI assistants which may indicate malware using AI for malicious purposes.
 * Reference: https://www.appdome.com/how-to/mobile-malware-prevention/android-malware-detection/detect-ai-assistant-malware-using-ai/
 */
@Singleton
class UnauthorizedAIAssistantDetectedHandler @Inject constructor() {

    companion object {
        private const val TAG = "UnauthorizedAIAssistant"
    }

    fun handleUnauthorizedAIAssistantDetectedEvent(threatEventData: ThreatEventData) {
        val sanitizedData = "id=${threatEventData.deviceID}, type=${threatEventData.threatCode}"
        Log.w(TAG, "Unauthorized AI Assistant Detected: $sanitizedData")
    }
}