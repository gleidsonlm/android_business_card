package com.gleidsonlm.businesscard

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Build
import android.util.Log
import com.gleidsonlm.businesscard.model.ThreatEventData
import com.gleidsonlm.businesscard.security.BotDefenseHandler
import com.gleidsonlm.businesscard.security.GoogleEmulatorHandler
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

/**
 * A [BroadcastReceiver] that listens for Appdome threat events.
 *
 * This receiver is responsible for registering to specific threat event broadcasts,
 * extracting threat data from the received intents, and launching the
 * [ThreatEventActivity] to display the information. It also handles MobileBotDefenseCheck
 * events through the [BotDefenseHandler].
 *
 * @property applicationContext The context of the application.
 */
class ThreatEventReceiver(private val applicationContext: Context) {

    private val TAG = "Appdome ThreatEvent"

    // Bot defense handler will be injected when available
    private var botDefenseHandler: BotDefenseHandler? = null
    private var googleEmulatorHandler: GoogleEmulatorHandler? = null

    fun setBotDefenseHandler(handler: BotDefenseHandler) {
        this.botDefenseHandler = handler
    }

    fun setGoogleEmulatorHandler(handler: GoogleEmulatorHandler) {
        this.googleEmulatorHandler = handler
    }

    private val receiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            onEvent(intent)
        }
    }

    /**
     * Registers the broadcast receiver for a predefined set of Appdome threat events.
     *
     * This method should be called, for example, in the `onCreate` method of the Application class.
     * It registers for events like "RootedDevice", "DeveloperOptionsEnabled", "DebuggerThreatDetected",
     * and "MobileBotDefenseCheck".
     */
    fun register() {
        // Register for specific threat events
        registerReceiverWithFlags(IntentFilter("RootedDevice"))
        registerReceiverWithFlags(IntentFilter("GoogleEmulatorDetected"))
        registerReceiverWithFlags(IntentFilter("DeveloperOptionsEnabled"))
        registerReceiverWithFlags(IntentFilter("DebuggerThreatDetected"))
        registerReceiverWithFlags(IntentFilter("MobileBotDefenseCheck"))
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
     * and then either handles it with the [BotDefenseHandler] for MobileBotDefenseCheck events
     * or starts the [ThreatEventActivity] to display the details of other threats.
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
            threatCode = intent.getStringExtra("threatCode"),
            threatName = intent.getStringExtra("threatName"),
            threatVendor = intent.getStringExtra("threatVendor"),
            threatVector = intent.getStringExtra("threatVector"),
            threatDescription = intent.getStringExtra("threatDescription"),
            threatRemediation = intent.getStringExtra("threatRemediation"),
            threatConfidentiality = intent.getStringExtra("threatConfidentiality"),
            threatIntegrity = intent.getStringExtra("threatIntegrity"),
            threatAvailability = intent.getStringExtra("threatAvailability"),
            threatSeverity = intent.getStringExtra("threatSeverity"),
            threatCVSS = intent.getStringExtra("threatCVSS"),
            threatCVE = intent.getStringExtra("threatCVE"),
            threatCWE = intent.getStringExtra("threatCWE"),
            threatCAPEC = intent.getStringExtra("threatCAPEC"),
            threatATTACK = intent.getStringExtra("threatATTACK"),
            threatMITRE = intent.getStringExtra("threatMITRE"),
            threatOWASP = intent.getStringExtra("threatOWASP"),
            threatNIST = intent.getStringExtra("threatNIST"),
            threatPCI = intent.getStringExtra("threatPCI"),
            threatHIPAA = intent.getStringExtra("threatHIPAA"),
            threatGDPR = intent.getStringExtra("threatGDPR"),
            threatCCPA = intent.getStringExtra("threatCCPA"),
            threatPII = intent.getStringExtra("threatPII"),
            threatPHI = intent.getStringExtra("threatPHI"),
            threatSPI = intent.getStringExtra("threatSPI"),
            threatFINRA = intent.getStringExtra("threatFINRA"),
            threatSOX = intent.getStringExtra("threatSOX"),
            threatFISMA = intent.getStringExtra("threatFISMA"),
            threatNERC = intent.getStringExtra("threatNERC"),
            threatFERC = intent.getStringExtra("threatFERC"),
            threatCOBIT = intent.getStringExtra("threatCOBIT"),
            threatISO = intent.getStringExtra("threatISO"),
            threatISA = intent.getStringExtra("threatISA"),
            threatIEC = intent.getStringExtra("threatIEC"),
            threatIEEE = intent.getStringExtra("threatIEEE"),
            threatICS = intent.getStringExtra("threatICS"),
            threatSCADA = intent.getStringExtra("threatSCADA"),
            threatPLC = intent.getStringExtra("threatPLC"),
            threatDCS = intent.getStringExtra("threatDCS"),
            threatRTU = intent.getStringExtra("threatRTU"),
            threatHMI = intent.getStringExtra("threatHMI"),
            threatSIS = intent.getStringExtra("threatSIS"),
            threatEWS = intent.getStringExtra("threatEWS"),
            threatMES = intent.getStringExtra("threatMES"),
            threatERP = intent.getStringExtra("threatERP"),
            threatCRM = intent.getStringExtra("threatCRM"),
            threatSCM = intent.getStringExtra("threatSCM"),
            threatPLM = intent.getStringExtra("threatPLM"),
            threatMOM = intent.getStringExtra("threatMOM"),
            threatBOM = intent.getStringExtra("threatBOM"),
            threatEAM = intent.getStringExtra("threatEAM"),
            threatCMMS = intent.getStringExtra("threatCMMS"),
            threatFSM = intent.getStringExtra("threatFSM"),
            threatWMS = intent.getStringExtra("threatWMS"),
            threatTMS = intent.getStringExtra("threatTMS"),
            threatLMS = intent.getStringExtra("threatLMS"),
            threatOMS = intent.getStringExtra("threatOMS"),
            threatPOS = intent.getStringExtra("threatPOS"),
            threatKIOSK = intent.getStringExtra("threatKIOSK"),
            threatATM = intent.getStringExtra("threatATM"),
            threatVENDING = intent.getStringExtra("threatVENDING"),
            threatGAMING = intent.getStringExtra("threatGAMING"),
            threatLOTTERY = intent.getStringExtra("threatLOTTERY"),
            threatVOTING = intent.getStringExtra("threatVOTING"),
            threatCASINO = intent.getStringExtra("threatCASINO"),
            threatSPORTS = intent.getStringExtra("threatSPORTS"),
            threatBETTING = intent.getStringExtra("threatBETTING"),
            threatFANTASY = intent.getStringExtra("threatFANTASY"),
            threatPOKER = intent.getStringExtra("threatPOKER"),
            threatBINGO = intent.getStringExtra("threatBINGO"),
            threatROULETTE = intent.getStringExtra("threatROULETTE"),
            threatBLACKJACK = intent.getStringExtra("threatBLACKJACK"),
            threatSLOTS = intent.getStringExtra("threatSLOTS"),
            threatBACCARAT = intent.getStringExtra("threatBACCARAT"),
            threatCRAPS = intent.getStringExtra("threatCRAPS"),
            threatKENO = intent.getStringExtra("threatKENO"),
            threatPAIGOW = intent.getStringExtra("threatPAIGOW"),
            threatSCRATCH = intent.getStringExtra("threatSCRATCH"),
            threatWHEEL = intent.getStringExtra("threatWHEEL"),
            threatRACING = intent.getStringExtra("threatRACING"),
            threatFIGHTING = intent.getStringExtra("threatFIGHTING"),
            threatSHOOTING = intent.getStringExtra("threatSHOOTING"),
            threatSTRATEGY = intent.getStringExtra("threatSTRATEGY"),
            threatPUZZLE = intent.getStringExtra("threatPUZZLE"),
            threatADVENTURE = intent.getStringExtra("threatADVENTURE"),
            threatARCADE = intent.getStringExtra("threatARCADE"),
            threatSIMULATION = intent.getStringExtra("threatSIMULATION"),
            threatROLEPLAYING = intent.getStringExtra("threatROLEPLAYING"),
            threatEDUCATIONAL = intent.getStringExtra("threatEDUCATIONAL"),
            threatFAMILY = intent.getStringExtra("threatFAMILY"),
            threatMUSIC = intent.getStringExtra("threatMUSIC"),
            threatWORD = intent.getStringExtra("threatWORD"),
            threatTRIVIA = intent.getStringExtra("threatTRIVIA"),
            threatCARD = intent.getStringExtra("threatCARD"),
            threatBOARD = intent.getStringExtra("threatBOARD"),
            threatCASUAL = intent.getStringExtra("threatCASUAL"),
            threatHYPERCASUAL = intent.getStringExtra("threatHYPERCASUAL"),
            threatMIDCORE = intent.getStringExtra("threatMIDCORE"),
            threatHARDCORE = intent.getStringExtra("threatHARDCORE"),
            threatESPORTS = intent.getStringExtra("threatESPORTS"),
            threatSTREAMING = intent.getStringExtra("threatSTREAMING"),
            threatVR = intent.getStringExtra("threatVR"),
            threatAR = intent.getStringExtra("threatAR"),
            threatMR = intent.getStringExtra("threatMR"),
            threatXR = intent.getStringExtra("threatXR"),
            threatMETAVERSE = intent.getStringExtra("threatMETAVERSE")
        )

        Log.d(TAG, "Populated ThreatEventData: $threatEventData")

        when (action) {
            "MobileBotDefenseCheck" -> handleBotDefenseCheck(threatEventData)
            "GoogleEmulatorDetected" -> {
                googleEmulatorHandler?.handleGoogleEmulatorEvent(threatEventData)
                routeToThreatEventActivity(threatEventData, action)
            }
            else -> {
                // Route other threat events to ThreatEventActivity
                routeToThreatEventActivity(threatEventData, action)
            }
        }
    }

    /**
     * Handles MobileBotDefenseCheck events using the BotDefenseHandler.
     */
    private fun handleBotDefenseCheck(threatEventData: ThreatEventData) {
        Log.i(TAG, "Handling MobileBotDefenseCheck event")
        
        botDefenseHandler?.handleBotDetectionEvent(
            threatEventData = threatEventData,
            callback = object : com.gleidsonlm.businesscard.security.BotDetectionCallback {
                override fun onDetectionComplete(action: com.gleidsonlm.businesscard.security.BotResponseAction) {
                    Log.i(TAG, "Bot detection completed with action: $action")
                    // Optionally route to ThreatEventActivity for user display
                    if (action != com.gleidsonlm.businesscard.security.BotResponseAction.LOG_ONLY) {
                        routeToThreatEventActivity(threatEventData, "MobileBotDefenseCheck")
                    }
                }

                override fun onUserNotificationRequired(message: String) {
                    Log.i(TAG, "User notification required: $message")
                    // Create enhanced threat event data with user message
                    val enhancedData = threatEventData.copy(
                        message = message,
                        defaultMessage = message
                    )
                    routeToThreatEventActivity(enhancedData, "MobileBotDefenseCheck")
                }

                override fun onSecurityCountermeasuresTriggered() {
                    Log.w(TAG, "Security countermeasures triggered for bot defense")
                    routeToThreatEventActivity(threatEventData, "MobileBotDefenseCheck")
                }

                override fun onCriticalThreatDetected(threatEventData: ThreatEventData) {
                    Log.e(TAG, "Critical bot threat detected")
                    val criticalData = threatEventData.copy(
                        message = "CRITICAL: Automated threat detected. App security measures activated.",
                        defaultMessage = "Critical security event - potential bot activity detected."
                    )
                    routeToThreatEventActivity(criticalData, "MobileBotDefenseCheck")
                }

                override fun onError(exception: Exception) {
                    Log.e(TAG, "Error in bot detection", exception)
                    val errorData = threatEventData.copy(
                        message = "Bot detection error: ${exception.message}",
                        internalError = exception.message
                    )
                    routeToThreatEventActivity(errorData, "MobileBotDefenseCheck")
                }
            }
        ) ?: run {
            // Fallback if bot defense handler is not available
            Log.w(TAG, "BotDefenseHandler not available, routing to ThreatEventActivity")
            routeToThreatEventActivity(threatEventData, "MobileBotDefenseCheck")
        }
    }

    /**
     * Routes threat events to the ThreatEventActivity for display.
     */
    private fun routeToThreatEventActivity(threatEventData: ThreatEventData, action: String) {
        val activityIntent = Intent(applicationContext, ThreatEventActivity::class.java).apply {
            putExtra(ThreatEventActivity.EXTRA_THREAT_EVENT_DATA, threatEventData)
            // FLAG_ACTIVITY_NEW_TASK is required when starting an activity from a context outside of an activity (like a BroadcastReceiver).
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        applicationContext.startActivity(activityIntent)
        Log.i(TAG, "Started ThreatEventActivity with data for action: $action")
    }
}
