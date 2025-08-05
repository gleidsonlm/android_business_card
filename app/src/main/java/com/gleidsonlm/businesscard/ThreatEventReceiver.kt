package com.gleidsonlm.businesscard

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Build
import android.util.Log
import androidx.core.content.ContextCompat
import com.gleidsonlm.businesscard.data.repository.ThreatEventRepository
import com.gleidsonlm.businesscard.model.ThreatEventData
import com.gleidsonlm.businesscard.security.BotDefenseHandler
import com.gleidsonlm.businesscard.security.ConditionalEnforcementEventHandler
import com.gleidsonlm.businesscard.security.ConditionalEnforcementResult
import com.gleidsonlm.businesscard.security.MobileBotDefenseRateLimitReachedHandler
import com.gleidsonlm.businesscard.security.NativeLibraryProtection
import com.gleidsonlm.businesscard.security.UpdateMBDMapHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.UUID

/**
 * A [BroadcastReceiver] that listens for Appdome threat events.
 *
 * This receiver is responsible for registering to specific threat event broadcasts,
 * extracting threat data from the received intents, saving them to the repository,
 * and launching the [ThreatEventActivity] to display the information. It also handles 
 * MobileBotDefenseCheck events through the [BotDefenseHandler], MobileBotDefenseRateLimitReached
 * events through the [MobileBotDefenseRateLimitReachedHandler], and UpdateMBDMap events 
 * through the [UpdateMBDMapHandler].
 *
 * @property applicationContext The context of the application.
 * @property threatEventRepository The repository for storing threat events.
 */
class ThreatEventReceiver(
    private val applicationContext: Context,
    private val threatEventRepository: ThreatEventRepository
) {

    private val TAG = "Appdome ThreatEvent"
    private val coroutineScope = CoroutineScope(Dispatchers.IO)

    private var botDefenseHandler: BotDefenseHandler? = null
    private var mobileBotDefenseRateLimitReachedHandler: MobileBotDefenseRateLimitReachedHandler? = null
    private var updateMBDMapHandler: UpdateMBDMapHandler? = null
    private var conditionalEnforcementEventHandler: ConditionalEnforcementEventHandler? = null
    private val threatHandlers = mutableMapOf<String, (ThreatEventData) -> Unit>()

    fun setBotDefenseHandler(handler: BotDefenseHandler) {
        this.botDefenseHandler = handler
    }

    fun setMobileBotDefenseRateLimitReachedHandler(handler: MobileBotDefenseRateLimitReachedHandler) {
        this.mobileBotDefenseRateLimitReachedHandler = handler
    }

    fun setUpdateMBDMapHandler(handler: UpdateMBDMapHandler) {
        this.updateMBDMapHandler = handler
    }

    fun setConditionalEnforcementEventHandler(handler: ConditionalEnforcementEventHandler) {
        this.conditionalEnforcementEventHandler = handler
    }

    fun addHandler(action: String, handler: (ThreatEventData) -> Unit) {
        threatHandlers[action] = handler
    }

    private val receiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            // Protect against native library crashes during threat event processing
            NativeLibraryProtection.safeNativeOperation(
                operation = "ThreatEvent onReceive for action: ${intent.action}",
                fallback = {
                    Log.e(TAG, "Failed to process threat event safely, logging for review")
                    // Still try to save basic event data for troubleshooting
                    val basicEventData = ThreatEventData(
                        id = UUID.randomUUID().toString(),
                        eventType = intent.action ?: "Unknown",
                        receivedTimestamp = System.currentTimeMillis(),
                        defaultMessage = "Event processing failed due to native library error",
                        internalError = "Native library protection triggered",
                        timeStamp = null,
                        deviceID = null,
                        deviceModel = null,
                        osVersion = null,
                        kernelInfo = null,
                        deviceManufacturer = null,
                        fusedAppToken = null,
                        carrierPlmn = null,
                        deviceBrand = null,
                        deviceBoard = null,
                        buildHost = null,
                        buildUser = null,
                        sdkVersion = null,
                        message = null,
                        failSafeEnforce = null,
                        externalID = null,
                        reasonCode = null,
                        buildDate = null,
                        devicePlatform = null,
                        carrierName = null,
                        updatedOSVersion = null,
                        timeZone = null,
                        deviceFaceDown = null,
                        locationLong = null,
                        locationLat = null,
                        locationState = null,
                        wifiSsid = null,
                        wifiSsidPermissionStatus = null,
                        threatCode = null
                    )
                    coroutineScope.launch {
                        try {
                            threatEventRepository.saveEvent(basicEventData)
                        } catch (e: Exception) {
                            Log.e(TAG, "Failed to save fallback event data", e)
                        }
                    }
                }
            ) {
                onEvent(intent)
            }
        }
    }

    /**
     * Registers the broadcast receiver for a predefined set of Appdome threat events.
     *
     * This method should be called, for example, in the `onCreate` method of the Application class.
     * It registers for events like "RootedDevice", "DeveloperOptionsEnabled", "DebuggerThreatDetected",
     * "MobileBotDefenseCheck", "MobileBotDefenseRateLimitReached", "UpdateMBDMap", and the new 
     * Anti-Malware threat events.
     */
    fun register() {
        // Register for existing threat events
        registerReceiverWithFlags(IntentFilter("RootedDevice"))
        registerReceiverWithFlags(IntentFilter("GoogleEmulatorDetected"))
        registerReceiverWithFlags(IntentFilter("DeveloperOptionsEnabled"))
        registerReceiverWithFlags(IntentFilter("DebuggerThreatDetected"))
        registerReceiverWithFlags(IntentFilter("MobileBotDefenseCheck"))
        registerReceiverWithFlags(IntentFilter("UnknownSourcesEnabled"))
        registerReceiverWithFlags(IntentFilter("AppIsDebuggable"))
        registerReceiverWithFlags(IntentFilter("AppIntegrityError"))
        registerReceiverWithFlags(IntentFilter("EmulatorFound"))
        
        // Register for new Anti-Malware threat events
        registerReceiverWithFlags(IntentFilter("DetectUnlockedBootloader"))
        registerReceiverWithFlags(IntentFilter("KernelSUDetected"))
        registerReceiverWithFlags(IntentFilter("OsRemountDetected"))
        registerReceiverWithFlags(IntentFilter("InjectedShellCodeDetected"))
        registerReceiverWithFlags(IntentFilter("UnauthorizedAIAssistantDetected"))
        registerReceiverWithFlags(IntentFilter("HookFrameworkDetected"))
        registerReceiverWithFlags(IntentFilter("MagiskManagerDetected"))
        registerReceiverWithFlags(IntentFilter("FridaDetected"))
        registerReceiverWithFlags(IntentFilter("FridaCustomDetected"))
        registerReceiverWithFlags(IntentFilter("SslIntegrityCheckFail"))
        registerReceiverWithFlags(IntentFilter("MalwareInjectionDetected"))
        
        // Register for additional required threat events
        registerReceiverWithFlags(IntentFilter("SslCertificateValidationFailed"))
        registerReceiverWithFlags(IntentFilter("SslNonSslConnection"))
        registerReceiverWithFlags(IntentFilter("SslIncompatibleVersion"))
        registerReceiverWithFlags(IntentFilter("NetworkProxyConfigured"))
        registerReceiverWithFlags(IntentFilter("ClickBotDetected"))
        registerReceiverWithFlags(IntentFilter("ClickBotDetectedByPermissions"))
        registerReceiverWithFlags(IntentFilter("KeyInjectionDetected"))
        registerReceiverWithFlags(IntentFilter("ActiveADBDetected"))
        registerReceiverWithFlags(IntentFilter("BlockSecondSpace"))
        registerReceiverWithFlags(IntentFilter("RunningInVirtualSpace"))
        registerReceiverWithFlags(IntentFilter("SeccompDetected"))
        registerReceiverWithFlags(IntentFilter("CorelliumFileFound"))
        registerReceiverWithFlags(IntentFilter("NotInstalledFromOfficialStore"))
        registerReceiverWithFlags(IntentFilter("GameGuardianDetected"))
        registerReceiverWithFlags(IntentFilter("SpeedHackDetected"))
        registerReceiverWithFlags(IntentFilter("CodeInjectionDetected"))
        registerReceiverWithFlags(IntentFilter("OatIntegrityBadCommandLine"))
        registerReceiverWithFlags(IntentFilter("RuntimeBundleValidationViolation"))
        
        // Register for new additional threat events from issue #60
        registerReceiverWithFlags(IntentFilter("SslServerCertificatePinningFailed"))
        registerReceiverWithFlags(IntentFilter("VulnerableUriDetected"))
        registerReceiverWithFlags(IntentFilter("FaceIDBypassDetected"))
        registerReceiverWithFlags(IntentFilter("DeepFakeAppsDetected"))
        registerReceiverWithFlags(IntentFilter("ActivePhoneCallDetected"))
        registerReceiverWithFlags(IntentFilter("BlockedScreenCaptureEvent"))
        registerReceiverWithFlags(IntentFilter("ClickBotDetectedVirtualFinger"))
        registerReceiverWithFlags(IntentFilter("IllegalDisplayEvent"))
        registerReceiverWithFlags(IntentFilter("OverlayDetected"))
        registerReceiverWithFlags(IntentFilter("BlockedKeyboardEvent"))
        registerReceiverWithFlags(IntentFilter("RogueMDMChangeDetected"))
        
        // Register for new security compliance threat events from issue #68
        registerReceiverWithFlags(IntentFilter("BannedManufacturer"))
        registerReceiverWithFlags(IntentFilter("SslIncompatibleCipher"))
        registerReceiverWithFlags(IntentFilter("SslInvalidCertificateChain"))
        registerReceiverWithFlags(IntentFilter("SslInvalidMinRSASignature"))
        registerReceiverWithFlags(IntentFilter("SslInvalidMinECCSignature"))
        registerReceiverWithFlags(IntentFilter("SslInvalidMinDigest"))
        
        // Register for new Appdome threat events from issue #70
        registerReceiverWithFlags(IntentFilter("IllegalAccessibilityServiceEvent"))
        registerReceiverWithFlags(IntentFilter("SimStateInfo"))
        registerReceiverWithFlags(IntentFilter("ActiveDebuggerThreatDetected"))
        registerReceiverWithFlags(IntentFilter("BlockedClipboardEvent"))
        registerReceiverWithFlags(IntentFilter("ConditionalEnforcementEvent"))
        registerReceiverWithFlags(IntentFilter("DeepSeekDetected"))
        registerReceiverWithFlags(IntentFilter("BlockedATSModification"))
        registerReceiverWithFlags(IntentFilter("UACPresented"))
        registerReceiverWithFlags(IntentFilter("CloakAndDaggerCapableAppDetected"))
        registerReceiverWithFlags(IntentFilter("AbusiveAccessibilityServiceDetected"))
        registerReceiverWithFlags(IntentFilter("StalkerSpywareDetected"))
        
        // Register for geo-compliance threat events from issue #72
        registerReceiverWithFlags(IntentFilter("GeoLocationSpoofingDetected"))
        registerReceiverWithFlags(IntentFilter("GeoLocationMockByAppDetected"))
        registerReceiverWithFlags(IntentFilter("ActiveVpnDetected"))
        registerReceiverWithFlags(IntentFilter("NoSimPresent"))
        registerReceiverWithFlags(IntentFilter("TeleportationDetected"))
        registerReceiverWithFlags(IntentFilter("FraudulentLocationDetected"))
        registerReceiverWithFlags(IntentFilter("GeoFencingUnauthorizedLocation"))
        
        // Register for new Appdome threat events from issue #74
        registerReceiverWithFlags(IntentFilter("MobileBotDefenseRateLimitReached"))
        registerReceiverWithFlags(IntentFilter("UpdateMBDMap"))
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
            // Use ContextCompat for better compatibility with RECEIVER_NOT_EXPORTED flag
            ContextCompat.registerReceiver(
                applicationContext,
                this.receiver,
                intentFilter,
                ContextCompat.RECEIVER_NOT_EXPORTED
            )
            Log.i(TAG, "Successfully registered receiver for action: ${action ?: "Unknown Action"}")
        } catch (e: Exception) {
            Log.e(TAG, "Failed to register receiver for action: ${action ?: "Unknown Action"}", e)
        }
    }

    /**
     * Handles the received threat event intent with protection against native library crashes.
     *
     * This method is called when a registered threat event is broadcast. It extracts
     * all relevant threat metadata from the intent, populates a [ThreatEventData] object,
     * saves it to the repository, and then either handles it with the [BotDefenseHandler] 
     * for MobileBotDefenseCheck events or starts the [ThreatEventActivity] to display 
     * the details of other threats. Includes protection against RSA_size null pointer
     * crashes in Appdome's libloader.so.
     *
     * @param intent The intent containing the threat event data.
     */
    fun onEvent(intent: Intent) {
        val action = intent.action ?: return
        Log.i(TAG, "Threat event received: $action")

        // Validate intent parameters before processing to prevent native crashes
        val intentParams = mapOf(
            "action" to action,
            "defaultMessage" to intent.getStringExtra("defaultMessage"),
            "deviceID" to intent.getStringExtra("deviceID"),
            "fusedAppToken" to intent.getStringExtra("fusedAppToken")
        )
        
        if (!NativeLibraryProtection.validateNativeParameters(intentParams)) {
            Log.w(TAG, "Invalid parameters detected in threat event, using safe defaults")
        }

        val threatEventData = ThreatEventData(
            id = UUID.randomUUID().toString(),
            eventType = action,
            receivedTimestamp = System.currentTimeMillis(),
            defaultMessage = intent.getStringExtra("defaultMessage") ?: "Threat event: $action",
            internalError = intent.getStringExtra("internalError"),
            timeStamp = intent.getStringExtra("timestamp"),
            deviceID = intent.getStringExtra("deviceID") ?: "unknown",
            deviceModel = intent.getStringExtra("deviceModel"),
            osVersion = intent.getStringExtra("osVersion"),
            kernelInfo = intent.getStringExtra("kernelInfo"),
            deviceManufacturer = intent.getStringExtra("deviceManufacturer"),
            fusedAppToken = intent.getStringExtra("fusedAppToken") ?: "",
            carrierPlmn = intent.getStringExtra("carrierPlmn"),
            deviceBrand = intent.getStringExtra("deviceBrand"),
            deviceBoard = intent.getStringExtra("deviceBoard"),
            buildHost = intent.getStringExtra("buildHost"),
            buildUser = intent.getStringExtra("buildUser"),
            sdkVersion = intent.getStringExtra("sdkVersion"),
            message = intent.getStringExtra("message"),
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

        // Save the event to repository for the event list screen with protection
        coroutineScope.launch {
            val success = NativeLibraryProtection.safeSuspendOperation(
                operation = "Save threat event to repository"
            ) {
                threatEventRepository.saveEvent(threatEventData)
                Log.d(TAG, "Threat event saved to repository: ${threatEventData.id}")
            }
            
            if (!success) {
                Log.e(TAG, "Failed to save threat event to repository due to native library error")
            }
        }

        when (action) {
            "MobileBotDefenseCheck" -> handleBotDefenseCheck(threatEventData)
            "MobileBotDefenseRateLimitReached" -> handleMobileBotDefenseRateLimitReached(threatEventData)
            "UpdateMBDMap" -> handleUpdateMBDMap(threatEventData)
            "ConditionalEnforcementEvent" -> handleConditionalEnforcementEvent(threatEventData)
            else -> {
                // Apply protection to custom threat handlers as well
                NativeLibraryProtection.safeNativeOperation(
                    operation = "Custom threat handler for $action",
                    fallback = {
                        Log.w(TAG, "Custom threat handler failed for $action, event saved to list")
                    }
                ) {
                    threatHandlers[action]?.invoke(threatEventData)
                }
                // Note: Removed automatic navigation to ThreatEventActivity
                // Events will be displayed in the list screen and users can tap to view details
                Log.i(TAG, "Threat event processed and saved: $action")
            }
        }
    }

    /**
     * Handles MobileBotDefenseCheck events using the BotDefenseHandler with native library protection.
     */
    private fun handleBotDefenseCheck(threatEventData: ThreatEventData) {
        Log.i(TAG, "Handling MobileBotDefenseCheck event")
        
        // Use safe bot defense operation to prevent native crashes during heartbeat operations
        val success = NativeLibraryProtection.safeBotDefenseOperation("MobileBotDefenseCheck processing") {
            botDefenseHandler?.handleBotDetectionEvent(
                threatEventData = threatEventData,
                callback = object : com.gleidsonlm.businesscard.security.BotDetectionCallback {
                    override fun onDetectionComplete(action: com.gleidsonlm.businesscard.security.BotResponseAction) {
                        Log.i(TAG, "Bot detection completed with action: $action")
                        // Only show activity for actions that require immediate user attention
                        when (action) {
                            com.gleidsonlm.businesscard.security.BotResponseAction.SECURITY_MEASURES,
                            com.gleidsonlm.businesscard.security.BotResponseAction.APP_PROTECTION -> {
                                routeToThreatEventActivity(threatEventData, "MobileBotDefenseCheck")
                            }
                            else -> {
                                Log.i(TAG, "Bot detection event saved to list for user review: $action")
                            }
                        }
                    }

                    override fun onUserNotificationRequired(message: String) {
                        Log.i(TAG, "User notification required: $message")
                        // Create enhanced threat event data with user message and show immediately
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
                        // For errors, just log and save to list - don't interrupt user
                        Log.i(TAG, "Bot detection error saved to list for review")
                    }
                }
            ) ?: run {
                Log.w(TAG, "BotDefenseHandler not available")
                false
            }
        }
        
        if (!success) {
            // Fallback if bot defense handler is not available or native operation failed
            Log.w(TAG, "BotDefenseHandler operation failed or not available, event saved to list")
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

    /**
     * Handles MobileBotDefenseRateLimitReached events using the MobileBotDefenseRateLimitReachedHandler with protection.
     */
    private fun handleMobileBotDefenseRateLimitReached(threatEventData: ThreatEventData) {
        Log.i(TAG, "Handling MobileBotDefenseRateLimitReached event")
        
        NativeLibraryProtection.safeBotDefenseOperation("MobileBotDefenseRateLimitReached processing") {
            mobileBotDefenseRateLimitReachedHandler?.handleRateLimitReachedEvent(threatEventData) ?: run {
                Log.w(TAG, "MobileBotDefenseRateLimitReachedHandler not available")
            }
        }
    }

    /**
     * Handles UpdateMBDMap events using the UpdateMBDMapHandler with protection.
     */
    private fun handleUpdateMBDMap(threatEventData: ThreatEventData) {
        Log.i(TAG, "Handling UpdateMBDMap event")
        
        NativeLibraryProtection.safeBotDefenseOperation("UpdateMBDMap processing") {
            updateMBDMapHandler?.handleMBDMapUpdateEvent(threatEventData) ?: run {
                Log.w(TAG, "UpdateMBDMapHandler not available")
            }
        }
    }

    /**
     * Handles ConditionalEnforcementEvent with proper enforcement decision logic.
     * 
     * This method ensures that Conditional Enforcement events do NOT automatically
     * enforce (terminate) the app unless explicitly required by Appdome's logic.
     */
    private fun handleConditionalEnforcementEvent(threatEventData: ThreatEventData) {
        Log.i(TAG, "Handling ConditionalEnforcementEvent - analyzing enforcement decision")
        
        NativeLibraryProtection.safeNativeOperation(
            operation = "ConditionalEnforcementEvent processing",
            fallback = {
                Log.w(TAG, "ConditionalEnforcementEvent processing failed, event saved to list only")
            }
        ) {
            val enforcementResult = conditionalEnforcementEventHandler?.handleConditionalEnforcementEventEvent(threatEventData)
                ?: ConditionalEnforcementResult.LOG_ONLY
            
            when (enforcementResult) {
                ConditionalEnforcementResult.LOG_ONLY -> {
                    Log.i(TAG, "ConditionalEnforcementEvent logged only - app continues normally")
                    // Event is already saved to repository, no user interruption needed
                }
                ConditionalEnforcementResult.ENFORCE_WITH_NOTIFICATION -> {
                    Log.w(TAG, "ConditionalEnforcementEvent requires user notification")
                    // Only show to user when enforcement is actually required
                    val enforcementData = threatEventData.copy(
                        message = threatEventData.message ?: "Security policy enforcement required",
                        defaultMessage = "Conditional enforcement has determined that security measures are needed"
                    )
                    routeToThreatEventActivity(enforcementData, "ConditionalEnforcementEvent")
                }
            }
        }
    }
}
