package com.gleidsonlm.businesscard

import android.app.Application
import com.gleidsonlm.businesscard.security.AppIntegrityErrorHandler
import com.gleidsonlm.businesscard.security.AppIsDebuggableHandler
import com.gleidsonlm.businesscard.security.BotDefenseHandler
import com.gleidsonlm.businesscard.security.DetectUnlockedBootloaderHandler
import com.gleidsonlm.businesscard.security.EmulatorFoundHandler
import com.gleidsonlm.businesscard.security.FridaCustomDetectedHandler
import com.gleidsonlm.businesscard.security.FridaDetectedHandler
import com.gleidsonlm.businesscard.security.GoogleEmulatorHandler
import com.gleidsonlm.businesscard.security.HookFrameworkDetectedHandler
import com.gleidsonlm.businesscard.security.InjectedShellCodeDetectedHandler
import com.gleidsonlm.businesscard.security.KernelSUDetectedHandler
import com.gleidsonlm.businesscard.security.MagiskManagerDetectedHandler
import com.gleidsonlm.businesscard.security.MalwareInjectionDetectedHandler
import com.gleidsonlm.businesscard.security.OsRemountDetectedHandler
import com.gleidsonlm.businesscard.security.SslIntegrityCheckFailHandler
import com.gleidsonlm.businesscard.security.UnauthorizedAIAssistantDetectedHandler
import com.gleidsonlm.businesscard.security.UnknownSourcesEnabledHandler
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject

/**
 * Custom [Application] class for the Business Card application.
 *
 * This class is annotated with [HiltAndroidApp] to enable Hilt dependency injection.
 * It also initializes and registers the [ThreatEventReceiver] to listen for Appdome threat events
 * and configures all threat event handlers including the new Anti-Malware handlers.
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

    // New Anti-Malware threat event handlers
    @Inject
    lateinit var detectUnlockedBootloaderHandler: DetectUnlockedBootloaderHandler

    @Inject
    lateinit var kernelSUDetectedHandler: KernelSUDetectedHandler

    @Inject
    lateinit var osRemountDetectedHandler: OsRemountDetectedHandler

    @Inject
    lateinit var injectedShellCodeDetectedHandler: InjectedShellCodeDetectedHandler

    @Inject
    lateinit var unauthorizedAIAssistantDetectedHandler: UnauthorizedAIAssistantDetectedHandler

    @Inject
    lateinit var hookFrameworkDetectedHandler: HookFrameworkDetectedHandler

    @Inject
    lateinit var magiskManagerDetectedHandler: MagiskManagerDetectedHandler

    @Inject
    lateinit var fridaDetectedHandler: FridaDetectedHandler

    @Inject
    lateinit var fridaCustomDetectedHandler: FridaCustomDetectedHandler

    @Inject
    lateinit var sslIntegrityCheckFailHandler: SslIntegrityCheckFailHandler

    @Inject
    lateinit var malwareInjectionDetectedHandler: MalwareInjectionDetectedHandler

    /**
     * Called when the application is starting, before any other application objects have been created.
     *
     * This method initializes the [ThreatEventReceiver], configures it with the [BotDefenseHandler],
     * and registers all threat event handlers including the new Anti-Malware handlers.
     */
    override fun onCreate() {
        super.onCreate()
        threatEventReceiver = ThreatEventReceiver(this)

        threatEventReceiver.setBotDefenseHandler(botDefenseHandler)

        // Register existing threat event handlers
        threatEventReceiver.addHandler("UnknownSourcesEnabled", unknownSourcesEnabledHandler::handleUnknownSourcesEnabledEvent)
        threatEventReceiver.addHandler("AppIsDebuggable", appIsDebuggableHandler::handleAppIsDebuggableEvent)
        threatEventReceiver.addHandler("AppIntegrityError", appIntegrityErrorHandler::handleAppIntegrityErrorEvent)
        threatEventReceiver.addHandler("EmulatorFound", emulatorFoundHandler::handleEmulatorFoundEvent)
        threatEventReceiver.addHandler("GoogleEmulatorDetected", googleEmulatorHandler::handleGoogleEmulatorEvent)

        // Register new Anti-Malware threat event handlers
        threatEventReceiver.addHandler("DetectUnlockedBootloader", detectUnlockedBootloaderHandler::handleDetectUnlockedBootloaderEvent)
        threatEventReceiver.addHandler("KernelSUDetected", kernelSUDetectedHandler::handleKernelSUDetectedEvent)
        threatEventReceiver.addHandler("OsRemountDetected", osRemountDetectedHandler::handleOsRemountDetectedEvent)
        threatEventReceiver.addHandler("InjectedShellCodeDetected", injectedShellCodeDetectedHandler::handleInjectedShellCodeDetectedEvent)
        threatEventReceiver.addHandler("UnauthorizedAIAssistantDetected", unauthorizedAIAssistantDetectedHandler::handleUnauthorizedAIAssistantDetectedEvent)
        threatEventReceiver.addHandler("HookFrameworkDetected", hookFrameworkDetectedHandler::handleHookFrameworkDetectedEvent)
        threatEventReceiver.addHandler("MagiskManagerDetected", magiskManagerDetectedHandler::handleMagiskManagerDetectedEvent)
        threatEventReceiver.addHandler("FridaDetected", fridaDetectedHandler::handleFridaDetectedEvent)
        threatEventReceiver.addHandler("FridaCustomDetected", fridaCustomDetectedHandler::handleFridaCustomDetectedEvent)
        threatEventReceiver.addHandler("SslIntegrityCheckFail", sslIntegrityCheckFailHandler::handleSslIntegrityCheckFailEvent)
        threatEventReceiver.addHandler("MalwareInjectionDetected", malwareInjectionDetectedHandler::handleMalwareInjectionDetectedEvent)

        threatEventReceiver.register()
    }
}
