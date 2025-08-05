package com.gleidsonlm.businesscard

import android.app.Application
import com.gleidsonlm.businesscard.data.repository.ThreatEventRepository
import com.gleidsonlm.businesscard.security.AbusiveAccessibilityServiceDetectedHandler
import com.gleidsonlm.businesscard.security.ActiveADBDetectedHandler
import com.gleidsonlm.businesscard.security.ActiveDebuggerThreatDetectedHandler
import com.gleidsonlm.businesscard.security.ActivePhoneCallDetectedHandler
import com.gleidsonlm.businesscard.security.AppIntegrityErrorHandler
import com.gleidsonlm.businesscard.security.AppIsDebuggableHandler
import com.gleidsonlm.businesscard.security.BannedManufacturerHandler
import com.gleidsonlm.businesscard.security.BlockSecondSpaceHandler
import com.gleidsonlm.businesscard.security.BlockedATSModificationHandler
import com.gleidsonlm.businesscard.security.BlockedClipboardEventHandler
import com.gleidsonlm.businesscard.security.BlockedKeyboardEventHandler
import com.gleidsonlm.businesscard.security.BlockedScreenCaptureEventHandler
import com.gleidsonlm.businesscard.security.BotDefenseHandler
import com.gleidsonlm.businesscard.security.ClickBotDetectedByPermissionsHandler
import com.gleidsonlm.businesscard.security.ClickBotDetectedHandler
import com.gleidsonlm.businesscard.security.ClickBotDetectedVirtualFingerHandler
import com.gleidsonlm.businesscard.security.CloakAndDaggerCapableAppDetectedHandler
import com.gleidsonlm.businesscard.security.CodeInjectionDetectedHandler
import com.gleidsonlm.businesscard.security.ConditionalEnforcementEventHandler
import com.gleidsonlm.businesscard.security.CorelliumFileFoundHandler
import com.gleidsonlm.businesscard.security.DeepFakeAppsDetectedHandler
import com.gleidsonlm.businesscard.security.DeepSeekDetectedHandler
import com.gleidsonlm.businesscard.security.DetectUnlockedBootloaderHandler
import com.gleidsonlm.businesscard.security.EmulatorFoundHandler
import com.gleidsonlm.businesscard.security.FaceIDBypassDetectedHandler
import com.gleidsonlm.businesscard.security.FridaCustomDetectedHandler
import com.gleidsonlm.businesscard.security.FridaDetectedHandler
import com.gleidsonlm.businesscard.security.FraudulentLocationDetectedHandler
import com.gleidsonlm.businesscard.security.GameGuardianDetectedHandler
import com.gleidsonlm.businesscard.security.GeoFencingUnauthorizedLocationHandler
import com.gleidsonlm.businesscard.security.GeoLocationMockByAppDetectedHandler
import com.gleidsonlm.businesscard.security.GeoLocationSpoofingDetectedHandler
import com.gleidsonlm.businesscard.security.GoogleEmulatorHandler
import com.gleidsonlm.businesscard.security.HookFrameworkDetectedHandler
import com.gleidsonlm.businesscard.security.IllegalAccessibilityServiceEventHandler
import com.gleidsonlm.businesscard.security.IllegalDisplayEventHandler
import com.gleidsonlm.businesscard.security.InjectedShellCodeDetectedHandler
import com.gleidsonlm.businesscard.security.KernelSUDetectedHandler
import com.gleidsonlm.businesscard.security.KeyInjectionDetectedHandler
import com.gleidsonlm.businesscard.security.MagiskManagerDetectedHandler
import com.gleidsonlm.businesscard.security.MalwareInjectionDetectedHandler
import com.gleidsonlm.businesscard.security.MobileBotDefenseRateLimitReachedHandler
import com.gleidsonlm.businesscard.security.NativeLibraryProtection
import com.gleidsonlm.businesscard.security.NetworkProxyConfiguredHandler
import com.gleidsonlm.businesscard.security.NoSimPresentHandler
import com.gleidsonlm.businesscard.security.NotInstalledFromOfficialStoreHandler
import com.gleidsonlm.businesscard.security.OatIntegrityBadCommandLineHandler
import com.gleidsonlm.businesscard.security.OsRemountDetectedHandler
import com.gleidsonlm.businesscard.security.OverlayDetectedHandler
import com.gleidsonlm.businesscard.security.RogueMDMChangeDetectedHandler
import com.gleidsonlm.businesscard.security.RunningInVirtualSpaceHandler
import com.gleidsonlm.businesscard.security.RuntimeBundleValidationViolationHandler
import com.gleidsonlm.businesscard.security.SeccompDetectedHandler
import com.gleidsonlm.businesscard.security.SimStateInfoHandler
import com.gleidsonlm.businesscard.security.SpeedHackDetectedHandler
import com.gleidsonlm.businesscard.security.SslCertificateValidationFailedHandler
import com.gleidsonlm.businesscard.security.SslIncompatibleCipherHandler
import com.gleidsonlm.businesscard.security.SslIncompatibleVersionHandler
import com.gleidsonlm.businesscard.security.SslIntegrityCheckFailHandler
import com.gleidsonlm.businesscard.security.SslInvalidCertificateChainHandler
import com.gleidsonlm.businesscard.security.SslInvalidMinDigestHandler
import com.gleidsonlm.businesscard.security.SslInvalidMinECCSignatureHandler
import com.gleidsonlm.businesscard.security.SslInvalidMinRSASignatureHandler
import com.gleidsonlm.businesscard.security.SslNonSslConnectionHandler
import com.gleidsonlm.businesscard.security.SslServerCertificatePinningFailedHandler
import com.gleidsonlm.businesscard.security.StalkerSpywareDetectedHandler
import com.gleidsonlm.businesscard.security.TeleportationDetectedHandler
import com.gleidsonlm.businesscard.security.UACPresentedHandler
import com.gleidsonlm.businesscard.security.UnauthorizedAIAssistantDetectedHandler
import com.gleidsonlm.businesscard.security.UnknownSourcesEnabledHandler
import com.gleidsonlm.businesscard.security.UpdateMBDMapHandler
import com.gleidsonlm.businesscard.security.VulnerableUriDetectedHandler
import com.gleidsonlm.businesscard.security.ActiveVpnDetectedHandler
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject

/**
 * Custom [Application] class for the Business Card application.
 *
 * This class is annotated with [HiltAndroidApp] to enable Hilt dependency injection.
 * It also initializes and registers the [ThreatEventReceiver] to listen for Appdome threat events
 * and configures all threat event handlers including Anti-Malware, SSL/TLS security, 
 * fraud prevention, and anti-cheat handlers.
 */
@HiltAndroidApp
class BusinessCardApplication : Application() {

    private lateinit var threatEventReceiver: ThreatEventReceiver

    @Inject
    lateinit var threatEventRepository: ThreatEventRepository

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

    // New additional threat event handlers
    @Inject
    lateinit var sslCertificateValidationFailedHandler: SslCertificateValidationFailedHandler

    @Inject
    lateinit var sslNonSslConnectionHandler: SslNonSslConnectionHandler

    @Inject
    lateinit var sslIncompatibleVersionHandler: SslIncompatibleVersionHandler

    @Inject
    lateinit var networkProxyConfiguredHandler: NetworkProxyConfiguredHandler

    @Inject
    lateinit var clickBotDetectedHandler: ClickBotDetectedHandler

    @Inject
    lateinit var clickBotDetectedByPermissionsHandler: ClickBotDetectedByPermissionsHandler

    @Inject
    lateinit var keyInjectionDetectedHandler: KeyInjectionDetectedHandler

    @Inject
    lateinit var activeADBDetectedHandler: ActiveADBDetectedHandler

    @Inject
    lateinit var blockSecondSpaceHandler: BlockSecondSpaceHandler

    @Inject
    lateinit var runningInVirtualSpaceHandler: RunningInVirtualSpaceHandler

    @Inject
    lateinit var seccompDetectedHandler: SeccompDetectedHandler

    @Inject
    lateinit var corelliumFileFoundHandler: CorelliumFileFoundHandler

    @Inject
    lateinit var notInstalledFromOfficialStoreHandler: NotInstalledFromOfficialStoreHandler

    @Inject
    lateinit var gameGuardianDetectedHandler: GameGuardianDetectedHandler

    @Inject
    lateinit var speedHackDetectedHandler: SpeedHackDetectedHandler

    @Inject
    lateinit var codeInjectionDetectedHandler: CodeInjectionDetectedHandler

    @Inject
    lateinit var oatIntegrityBadCommandLineHandler: OatIntegrityBadCommandLineHandler

    @Inject
    lateinit var runtimeBundleValidationViolationHandler: RuntimeBundleValidationViolationHandler

    // New threat event handlers from issue #60
    @Inject
    lateinit var sslServerCertificatePinningFailedHandler: SslServerCertificatePinningFailedHandler

    @Inject
    lateinit var vulnerableUriDetectedHandler: VulnerableUriDetectedHandler

    @Inject
    lateinit var faceIDBypassDetectedHandler: FaceIDBypassDetectedHandler

    @Inject
    lateinit var deepFakeAppsDetectedHandler: DeepFakeAppsDetectedHandler

    @Inject
    lateinit var activePhoneCallDetectedHandler: ActivePhoneCallDetectedHandler

    @Inject
    lateinit var blockedScreenCaptureEventHandler: BlockedScreenCaptureEventHandler

    @Inject
    lateinit var clickBotDetectedVirtualFingerHandler: ClickBotDetectedVirtualFingerHandler

    @Inject
    lateinit var illegalDisplayEventHandler: IllegalDisplayEventHandler

    @Inject
    lateinit var overlayDetectedHandler: OverlayDetectedHandler

    @Inject
    lateinit var blockedKeyboardEventHandler: BlockedKeyboardEventHandler

    @Inject
    lateinit var rogueMDMChangeDetectedHandler: RogueMDMChangeDetectedHandler

    // New security compliance threat event handlers from issue #68
    @Inject
    lateinit var bannedManufacturerHandler: BannedManufacturerHandler

    @Inject
    lateinit var sslIncompatibleCipherHandler: SslIncompatibleCipherHandler

    @Inject
    lateinit var sslInvalidCertificateChainHandler: SslInvalidCertificateChainHandler

    @Inject
    lateinit var sslInvalidMinRSASignatureHandler: SslInvalidMinRSASignatureHandler

    @Inject
    lateinit var sslInvalidMinECCSignatureHandler: SslInvalidMinECCSignatureHandler

    @Inject
    lateinit var sslInvalidMinDigestHandler: SslInvalidMinDigestHandler

    // New Appdome threat event handlers from issue #70
    @Inject
    lateinit var illegalAccessibilityServiceEventHandler: IllegalAccessibilityServiceEventHandler

    @Inject
    lateinit var simStateInfoHandler: SimStateInfoHandler

    @Inject
    lateinit var activeDebuggerThreatDetectedHandler: ActiveDebuggerThreatDetectedHandler

    @Inject
    lateinit var blockedClipboardEventHandler: BlockedClipboardEventHandler

    @Inject
    lateinit var conditionalEnforcementEventHandler: ConditionalEnforcementEventHandler

    @Inject
    lateinit var deepSeekDetectedHandler: DeepSeekDetectedHandler

    @Inject
    lateinit var blockedATSModificationHandler: BlockedATSModificationHandler

    @Inject
    lateinit var uacPresentedHandler: UACPresentedHandler

    @Inject
    lateinit var cloakAndDaggerCapableAppDetectedHandler: CloakAndDaggerCapableAppDetectedHandler

    @Inject
    lateinit var abusiveAccessibilityServiceDetectedHandler: AbusiveAccessibilityServiceDetectedHandler

    @Inject
    lateinit var stalkerSpywareDetectedHandler: StalkerSpywareDetectedHandler

    // New geo-compliance threat event handlers from issue #72
    @Inject
    lateinit var geoLocationSpoofingDetectedHandler: GeoLocationSpoofingDetectedHandler

    @Inject
    lateinit var geoLocationMockByAppDetectedHandler: GeoLocationMockByAppDetectedHandler

    @Inject
    lateinit var activeVpnDetectedHandler: ActiveVpnDetectedHandler

    @Inject
    lateinit var noSimPresentHandler: NoSimPresentHandler

    @Inject
    lateinit var teleportationDetectedHandler: TeleportationDetectedHandler

    @Inject
    lateinit var fraudulentLocationDetectedHandler: FraudulentLocationDetectedHandler

    @Inject
    lateinit var geoFencingUnauthorizedLocationHandler: GeoFencingUnauthorizedLocationHandler

    // New Appdome threat event handlers from issue #74
    @Inject
    lateinit var mobileBotDefenseRateLimitReachedHandler: MobileBotDefenseRateLimitReachedHandler

    @Inject
    lateinit var updateMBDMapHandler: UpdateMBDMapHandler

    /**
     * Called when the application is starting, before any other application objects have been created.
     *
     * This method initializes native library protection against crashes, the [ThreatEventReceiver], 
     * configures it with the [BotDefenseHandler], and registers all threat event handlers including 
     * Anti-Malware, SSL/TLS security, fraud prevention, and anti-cheat handlers.
     */
    override fun onCreate() {
        super.onCreate()
        
        // Initialize native library protection first to prevent crashes during startup
        NativeLibraryProtection.initialize()
        
        threatEventReceiver = ThreatEventReceiver(this, threatEventRepository)

        threatEventReceiver.setBotDefenseHandler(botDefenseHandler)
        threatEventReceiver.setMobileBotDefenseRateLimitReachedHandler(mobileBotDefenseRateLimitReachedHandler)
        threatEventReceiver.setUpdateMBDMapHandler(updateMBDMapHandler)

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

        // Register additional required threat event handlers
        threatEventReceiver.addHandler("SslCertificateValidationFailed", sslCertificateValidationFailedHandler::handleSslCertificateValidationFailedEvent)
        threatEventReceiver.addHandler("SslNonSslConnection", sslNonSslConnectionHandler::handleSslNonSslConnectionEvent)
        threatEventReceiver.addHandler("SslIncompatibleVersion", sslIncompatibleVersionHandler::handleSslIncompatibleVersionEvent)
        threatEventReceiver.addHandler("NetworkProxyConfigured", networkProxyConfiguredHandler::handleNetworkProxyConfiguredEvent)
        threatEventReceiver.addHandler("ClickBotDetected", clickBotDetectedHandler::handleClickBotDetectedEvent)
        threatEventReceiver.addHandler("ClickBotDetectedByPermissions", clickBotDetectedByPermissionsHandler::handleClickBotDetectedByPermissionsEvent)
        threatEventReceiver.addHandler("KeyInjectionDetected", keyInjectionDetectedHandler::handleKeyInjectionDetectedEvent)
        threatEventReceiver.addHandler("ActiveADBDetected", activeADBDetectedHandler::handleActiveADBDetectedEvent)
        threatEventReceiver.addHandler("BlockSecondSpace", blockSecondSpaceHandler::handleBlockSecondSpaceEvent)
        threatEventReceiver.addHandler("RunningInVirtualSpace", runningInVirtualSpaceHandler::handleRunningInVirtualSpaceEvent)
        threatEventReceiver.addHandler("SeccompDetected", seccompDetectedHandler::handleSeccompDetectedEvent)
        threatEventReceiver.addHandler("CorelliumFileFound", corelliumFileFoundHandler::handleCorelliumFileFoundEvent)
        threatEventReceiver.addHandler("NotInstalledFromOfficialStore", notInstalledFromOfficialStoreHandler::handleNotInstalledFromOfficialStoreEvent)
        threatEventReceiver.addHandler("GameGuardianDetected", gameGuardianDetectedHandler::handleGameGuardianDetectedEvent)
        threatEventReceiver.addHandler("SpeedHackDetected", speedHackDetectedHandler::handleSpeedHackDetectedEvent)
        threatEventReceiver.addHandler("CodeInjectionDetected", codeInjectionDetectedHandler::handleCodeInjectionDetectedEvent)
        threatEventReceiver.addHandler("OatIntegrityBadCommandLine", oatIntegrityBadCommandLineHandler::handleOatIntegrityBadCommandLineEvent)
        threatEventReceiver.addHandler("RuntimeBundleValidationViolation", runtimeBundleValidationViolationHandler::handleRuntimeBundleValidationViolationEvent)

        // Register new threat event handlers from issue #60
        threatEventReceiver.addHandler("SslServerCertificatePinningFailed", sslServerCertificatePinningFailedHandler::handleSslServerCertificatePinningFailedEvent)
        threatEventReceiver.addHandler("VulnerableUriDetected", vulnerableUriDetectedHandler::handleVulnerableUriDetectedEvent)
        threatEventReceiver.addHandler("FaceIDBypassDetected", faceIDBypassDetectedHandler::handleFaceIDBypassDetectedEvent)
        threatEventReceiver.addHandler("DeepFakeAppsDetected", deepFakeAppsDetectedHandler::handleDeepFakeAppsDetectedEvent)
        threatEventReceiver.addHandler("ActivePhoneCallDetected", activePhoneCallDetectedHandler::handleActivePhoneCallDetectedEvent)
        threatEventReceiver.addHandler("BlockedScreenCaptureEvent", blockedScreenCaptureEventHandler::handleBlockedScreenCaptureEventEvent)
        threatEventReceiver.addHandler("ClickBotDetectedVirtualFinger", clickBotDetectedVirtualFingerHandler::handleClickBotDetectedVirtualFingerEvent)
        threatEventReceiver.addHandler("IllegalDisplayEvent", illegalDisplayEventHandler::handleIllegalDisplayEventEvent)
        threatEventReceiver.addHandler("OverlayDetected", overlayDetectedHandler::handleOverlayDetectedEvent)
        threatEventReceiver.addHandler("BlockedKeyboardEvent", blockedKeyboardEventHandler::handleBlockedKeyboardEventEvent)
        threatEventReceiver.addHandler("RogueMDMChangeDetected", rogueMDMChangeDetectedHandler::handleRogueMDMChangeDetectedEvent)

        // Register new security compliance threat event handlers from issue #68
        threatEventReceiver.addHandler("BannedManufacturer", bannedManufacturerHandler::handleBannedManufacturerEvent)
        threatEventReceiver.addHandler("SslIncompatibleCipher", sslIncompatibleCipherHandler::handleSslIncompatibleCipherEvent)
        threatEventReceiver.addHandler("SslInvalidCertificateChain", sslInvalidCertificateChainHandler::handleSslInvalidCertificateChainEvent)
        threatEventReceiver.addHandler("SslInvalidMinRSASignature", sslInvalidMinRSASignatureHandler::handleSslInvalidMinRSASignatureEvent)
        threatEventReceiver.addHandler("SslInvalidMinECCSignature", sslInvalidMinECCSignatureHandler::handleSslInvalidMinECCSignatureEvent)
        threatEventReceiver.addHandler("SslInvalidMinDigest", sslInvalidMinDigestHandler::handleSslInvalidMinDigestEvent)

        // Register new Appdome threat event handlers from issue #70
        threatEventReceiver.addHandler("IllegalAccessibilityServiceEvent", illegalAccessibilityServiceEventHandler::handleIllegalAccessibilityServiceEventEvent)
        threatEventReceiver.addHandler("SimStateInfo", simStateInfoHandler::handleSimStateInfoEvent)
        threatEventReceiver.addHandler("ActiveDebuggerThreatDetected", activeDebuggerThreatDetectedHandler::handleActiveDebuggerThreatDetectedEvent)
        threatEventReceiver.addHandler("BlockedClipboardEvent", blockedClipboardEventHandler::handleBlockedClipboardEventEvent)
        threatEventReceiver.addHandler("ConditionalEnforcementEvent", conditionalEnforcementEventHandler::handleConditionalEnforcementEventEvent)
        threatEventReceiver.addHandler("DeepSeekDetected", deepSeekDetectedHandler::handleDeepSeekDetectedEvent)
        threatEventReceiver.addHandler("BlockedATSModification", blockedATSModificationHandler::handleBlockedATSModificationEvent)
        threatEventReceiver.addHandler("UACPresented", uacPresentedHandler::handleUACPresentedEvent)
        threatEventReceiver.addHandler("CloakAndDaggerCapableAppDetected", cloakAndDaggerCapableAppDetectedHandler::handleCloakAndDaggerCapableAppDetectedEvent)
        threatEventReceiver.addHandler("AbusiveAccessibilityServiceDetected", abusiveAccessibilityServiceDetectedHandler::handleAbusiveAccessibilityServiceDetectedEvent)
        threatEventReceiver.addHandler("StalkerSpywareDetected", stalkerSpywareDetectedHandler::handleStalkerSpywareDetectedEvent)

        // Register new geo-compliance threat event handlers from issue #72
        threatEventReceiver.addHandler("GeoLocationSpoofingDetected", geoLocationSpoofingDetectedHandler::handleGeoLocationSpoofingDetectedEvent)
        threatEventReceiver.addHandler("GeoLocationMockByAppDetected", geoLocationMockByAppDetectedHandler::handleGeoLocationMockByAppDetectedEvent)
        threatEventReceiver.addHandler("ActiveVpnDetected", activeVpnDetectedHandler::handleActiveVpnDetectedEvent)
        threatEventReceiver.addHandler("NoSimPresent", noSimPresentHandler::handleNoSimPresentEvent)
        threatEventReceiver.addHandler("TeleportationDetected", teleportationDetectedHandler::handleTeleportationDetectedEvent)
        threatEventReceiver.addHandler("FraudulentLocationDetected", fraudulentLocationDetectedHandler::handleFraudulentLocationDetectedEvent)
        threatEventReceiver.addHandler("GeoFencingUnauthorizedLocation", geoFencingUnauthorizedLocationHandler::handleGeoFencingUnauthorizedLocationEvent)

        threatEventReceiver.register()
    }
}
