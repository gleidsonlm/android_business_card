package com.gleidsonlm.businesscard.security

import android.util.Log
import com.gleidsonlm.businesscard.model.ThreatEventData
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Handler for RuntimeBundleValidationViolation threat events from Appdome.
 * 
 * Detects runtime bundle validation violations which may indicate dynamic modding.
 * Reference: https://www.appdome.com/how-to/mobile-cheat-prevention/fake-app-trojan-defense/use-runtime-bundle-validation-to-prevent-dynamic-modding-in-android-ios-apps/
 */
@Singleton
class RuntimeBundleValidationViolationHandler @Inject constructor() {

    companion object {
        private const val TAG = "RuntimeBundleValidationViolation"
    }

    fun handleRuntimeBundleValidationViolationEvent(threatEventData: ThreatEventData) {
        val sanitizedData = "id=${threatEventData.deviceID}, type=${threatEventData.threatCode}"
        Log.w(TAG, "Runtime Bundle Validation Violation: $sanitizedData")
    }
}