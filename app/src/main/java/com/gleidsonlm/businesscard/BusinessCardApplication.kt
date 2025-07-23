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

    private lateinit var threatEventReceiver: ThreatEventReceiver

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
        threatEventReceiver = ThreatEventReceiver(this)

        threatEventReceiver.setBotDefenseHandler(botDefenseHandler)

        threatEventReceiver.addHandler("UnknownSourcesEnabled", unknownSourcesEnabledHandler::handleUnknownSourcesEnabledEvent)
        threatEventReceiver.addHandler("AppIsDebuggable", appIsDebuggableHandler::handleAppIsDebuggableEvent)
        threatEventReceiver.addHandler("AppIntegrityError", appIntegrityErrorHandler::handleAppIntegrityErrorEvent)
        threatEventReceiver.addHandler("EmulatorFound", emulatorFoundHandler::handleEmulatorFoundEvent)
        threatEventReceiver.addHandler("GoogleEmulatorDetected", googleEmulatorHandler::handleGoogleEmulatorEvent)


        threatEventReceiver.register()
    }
}
