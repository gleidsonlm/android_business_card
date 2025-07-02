package com.gleidsonlm.businesscard

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Build
import android.util.Log
import com.gleidsonlm.businesscard.model.ThreatEventData

/**
 * A [BroadcastReceiver] that listens for Appdome threat events.
 *
 * This receiver is responsible for registering to specific threat event broadcasts,
 * extracting threat data from the received intents, and launching the
 * [ThreatEventActivity] to display the information.
 *
 * @property applicationContext The context of the application.
 */
class ThreatEventReceiver(private val applicationContext: Context) {

    private val TAG = "Appdome ThreatEvent"

    private val receiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            onEvent(intent)
        }
    }

    /**
     * Registers the broadcast receiver for a predefined set of Appdome threat events.
     *
     * This method should be called, for example, in the `onCreate` method of the Application class.
     * It registers for events like "RootedDevice", "DeveloperOptionsEnabled", and "DebuggerThreatDetected".
     */
    fun register() {
        // Register for specific threat events
        registerReceiverWithFlags(IntentFilter("RootedDevice"))
        registerReceiverWithFlags(IntentFilter("DeveloperOptionsEnabled"))
        registerReceiverWithFlags(IntentFilter("DebuggerThreatDetected"))
        // Add other threat events if needed, ensuring they match the documentation and requirements
    }

    /**
     * Unregisters the broadcast receiver.
     *
     * It's good practice to call this method when the receiver is no longer needed,
     * though for a receiver tied to the Application lifecycle, it might only be called
     * if the application process is explicitly terminated.
     */
    fun unregister() {
        try {
            applicationContext.unregisterReceiver(receiver)
            Log.i(TAG, "ThreatEventReceiver unregistered successfully.")
        } catch (e: IllegalArgumentException) {
            // Receiver wasn't registered, ignore.
            Log.w(TAG, "ThreatEventReceiver was not registered or already unregistered.")
        }
    }

    private fun registerReceiverWithFlags(intentFilter: IntentFilter) {
        val action = intentFilter.getAction(0) // Get the first action, assuming one action per filter for logging
        Log.i(TAG, "Attempting to register receiver for action: ${action ?: "Unknown Action"}")
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                applicationContext.registerReceiver(
                    this.receiver,
                    intentFilter,
                    Context.RECEIVER_NOT_EXPORTED
                )
            } else {
                applicationContext.registerReceiver(this.receiver, intentFilter)
            }
            Log.i(TAG, "Successfully registered receiver for action: ${action ?: "Unknown Action"}")
        } catch (e: Exception) {
            Log.e(TAG, "Failed to register receiver for action: ${action ?: "Unknown Action"}", e)
        }
    }

    /**
     * Handles the received threat event intent.
     *
     * This method is called when a registered threat event is broadcast. It extracts
     * all relevant threat metadata from the intent, populates a [ThreatEventData] object,
     * and then starts the [ThreatEventActivity] to display the details of the threat.
     *
     * @param intent The intent containing the threat event data.
     */
    fun onEvent(intent: Intent) {
        val action = intent.action ?: return
        Log.i(TAG, "Threat event received: $action")

        // Extract all data based on the Appdome documentation's "Meta-Data for Mobile Application Threat-Events"
        // and the updated ThreatEventData model.
        // The string keys used in intent.getStringExtra(key) MUST exactly match the keys
        // sent by Appdome in the threat event Intent. Consult Appdome documentation for the definitive list of keys.
        val threatEventData = ThreatEventData(
            defaultMessage = intent.getStringExtra("defaultMessage"),
            internalError = intent.getStringExtra("internalError"),
            timeStamp = intent.getStringExtra("timestamp"), // Example: "timestamp" might be "timeStamp" or "eventTimestamp" in Appdome docs.
            deviceID = intent.getStringExtra("deviceID"),
            deviceModel = intent.getStringExtra("deviceModel"),
            osVersion = intent.getStringExtra("osVersion"),
            kernelInfo = intent.getStringExtra("kernelInfo"),
            deviceManufacturer = intent.getStringExtra("deviceManufacturer"),
            fusedAppToken = intent.getStringExtra("fusedAppToken"),
            carrierPlmn = intent.getStringExtra("carrierPlmn"),
            deviceBrand = intent.getStringExtra("deviceBrand"),
            deviceBoard = intent.getStringExtra("deviceBoard"),
            buildHost = intent.getStringExtra("buildHost"),
            buildUser = intent.getStringExtra("buildUser"),
            sdkVersion = intent.getStringExtra("sdkVersion"),
            message = intent.getStringExtra("message"), // Specific message for this event, if any
            failSafeEnforce = intent.getStringExtra("failSafeEnforce"),
            externalID = intent.getStringExtra("externalID"),
            reasonCode = intent.getStringExtra("reasonCode"),
            buildDate = intent.getStringExtra("buildDate"),
            devicePlatform = intent.getStringExtra("devicePlatform"),
            carrierName = intent.getStringExtra("carrierName"),
            updatedOSVersion = intent.getStringExtra("updatedOSVersion"),
            timeZone = intent.getStringExtra("timeZone"),
            deviceFaceDown = intent.getStringExtra("deviceFaceDown"),
            locationLong = intent.getStringExtra("locationLong"),
            locationLat = intent.getStringExtra("locationLat"),
            locationState = intent.getStringExtra("locationState"),
            wifiSsid = intent.getStringExtra("wifiSsid"),
            wifiSsidPermissionStatus = intent.getStringExtra("wifiSsidPermissionStatus"),
            threatCode = intent.getStringExtra("threatCode")
        )

        Log.d(TAG, "Populated ThreatEventData: $threatEventData")

        // Route to ThreatEventActivity
        val activityIntent = Intent(applicationContext, ThreatEventActivity::class.java).apply {
            putExtra(ThreatEventActivity.EXTRA_THREAT_EVENT_DATA, threatEventData)
            // FLAG_ACTIVITY_NEW_TASK is required when starting an activity from a context outside of an activity (like a BroadcastReceiver).
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        applicationContext.startActivity(activityIntent)
        Log.i(TAG, "Started ThreatEventActivity with data for action: $action")
    }
}
