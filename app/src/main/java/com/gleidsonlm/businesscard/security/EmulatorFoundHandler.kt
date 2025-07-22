package com.gleidsonlm.businesscard.security

import android.util.Log
import com.gleidsonlm.businesscard.model.ThreatEventData
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class EmulatorFoundHandler @Inject constructor() {

    private val TAG = "EmulatorFound"

    fun handleEmulatorFoundEvent(threatEventData: ThreatEventData) {
        Log.w(TAG, "Emulator Found Detected: $threatEventData")
    }
}
