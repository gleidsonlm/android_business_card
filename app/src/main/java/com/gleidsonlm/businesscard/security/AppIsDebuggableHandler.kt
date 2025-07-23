package com.gleidsonlm.businesscard.security

import android.util.Log
import com.gleidsonlm.businesscard.model.ThreatEventData
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AppIsDebuggableHandler @Inject constructor() {

    companion object {
        private const val TAG = "AppIsDebuggable"
    }

    fun handleAppIsDebuggableEvent(threatEventData: ThreatEventData) {
        Log.w(TAG, "App Is Debuggable Detected: $threatEventData")
    }
}
