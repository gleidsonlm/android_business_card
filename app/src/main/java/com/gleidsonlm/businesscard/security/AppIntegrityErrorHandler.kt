package com.gleidsonlm.businesscard.security

import android.util.Log
import com.gleidsonlm.businesscard.model.ThreatEventData
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AppIntegrityErrorHandler @Inject constructor() {

    private val TAG = "AppIntegrityError"

    fun handleAppIntegrityErrorEvent(threatEventData: ThreatEventData) {
        Log.w(TAG, "App Integrity Error Detected: $threatEventData")
    }
}
