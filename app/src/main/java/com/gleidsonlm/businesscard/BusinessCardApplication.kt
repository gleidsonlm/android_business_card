package com.gleidsonlm.businesscard

import android.app.Application
import com.gleidsonlm.businesscard.security.AppIntegrityErrorHandler
import com.gleidsonlm.businesscard.security.AppIsDebuggableHandler
import com.gleidsonlm.businesscard.security.BotDefenseHandler
import com.gleidsonlm.businesscard.security.EmulatorFoundHandler
import com.gleidsonlm.businesscard.security.GoogleEmulatorHandler
import com.gleidsonlm.businesscard.security.UnknownSourcesEnabledHandler
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject

/**
 * Custom [Application] class for the Business Card application.
 *
 * This class is annotated with [HiltAndroidApp] to enable Hilt dependency injection.
 * It also initializes and registers the [ThreatEventReceiver] to listen for Appdome threat events
 * and configures the [BotDefenseHandler] for MobileBotDefenseCheck events.
 */
@HiltAndroidApp
class BusinessCardApplication : Application() {

    private lateinit var threatEventsReceiver: ThreatEventReceiver

    @Inject
    lateinit var botDefenseHandler: BotDefenseHandler

    @Inject
    lateinit var googleEmulatorHandler: GoogleEmulatorHandler

    @Inject
    lateinit var unknownSourcesEnabledHandler: UnknownSourcesEnabledHandler

    @Inject
    lateinit var appIsDebuggableHandler: AppIsDebuggableHandler

    @Inject
    lateinit var appIntegrityErrorHandler: AppIntegrityErrorHandler

    @Inject
    lateinit var emulatorFoundHandler: EmulatorFoundHandler

    /**
     * Called when the application is starting, before any other application objects have been created.
     *
     * This method initializes the [ThreatEventReceiver], configures it with the [BotDefenseHandler],
     * and registers it to listen for Appdome threat events.
     */
    override fun onCreate() {
        super.onCreate()
        
        // Initialize and register the Appdome ThreatEventReceiver.
        // This allows the application to listen for security events detected by Appdome.
        threatEventsReceiver = ThreatEventReceiver(applicationContext)
        threatEventsReceiver.setBotDefenseHandler(botDefenseHandler)
        threatEventsReceiver.setGoogleEmulatorHandler(googleEmulatorHandler)
        threatEventsReceiver.setUnknownSourcesEnabledHandler(unknownSourcesEnabledHandler)
        threatEventsReceiver.setAppIsDebuggableHandler(appIsDebuggableHandler)
        threatEventsReceiver.setAppIntegrityErrorHandler(appIntegrityErrorHandler)
        threatEventsReceiver.setEmulatorFoundHandler(emulatorFoundHandler)
        threatEventsReceiver.register()
        
        // Note: Unregistration of an Application-level receiver is typically handled by Android
        // upon app termination and may not require an explicit call to unregister().
    }
}
