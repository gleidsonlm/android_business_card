package com.gleidsonlm.businesscard.security

import android.util.Log
import com.gleidsonlm.businesscard.model.ThreatEventData
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UnknownSourcesEnabledHandler @Inject constructor() {

    private val TAG = "UnknownSourcesEnabled"

    fun handleUnknownSourcesEnabledEvent(threatEventData: ThreatEventData) {
        Log.w(TAG, "Unknown Sources Enabled Detected: $threatEventData")
    }
}
