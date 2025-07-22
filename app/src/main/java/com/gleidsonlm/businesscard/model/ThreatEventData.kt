package com.gleidsonlm.businesscard.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize


/**
 * Data class representing the comprehensive meta-data associated with an Appdome Threat Event.
 *
 * This class is [Parcelable] to allow it to be passed between Android components,
 * such as from a [BroadcastReceiver] to an [Activity]. Each field corresponds to
 * a piece of information provided by Appdome when a threat event occurs.
 */
@Parcelize
data class ThreatEventData(
    /** Internal error code or message from Appdome. */
    val internalError: String?,
    /** Default message provided by Appdome for the threat event, suitable for user display. */
    val defaultMessage: String?,
    /** Timestamp of when the event occurred. */
    val timeStamp: String?,
    /** Unique identifier for the device. */
    val deviceID: String?,
    /** Model of the device (e.g., "Pixel 7 Pro"). */
    val deviceModel: String?,
    /** Version of the operating system (e.g., "13"). */
    val osVersion: String?,
    /** Information about the device's kernel. */
    val kernelInfo: String?,
    /** Manufacturer of the device (e.g., "Google"). */
    val deviceManufacturer: String?,
    /** Appdome's unique token for the fused application. */
    val fusedAppToken: String?,
    /** PLMN (Public Land Mobile Network) code of the carrier. */
    val carrierPlmn: String?,
    /** Brand of the device (e.g., "google"). */
    val deviceBrand: String?,
    /** Name of the device's main board. */
    val deviceBoard: String?,
    /** Hostname of the machine that built the application. */
    val buildHost: String?,
    /** User that built the application. */
    val buildUser: String?,
    /** SDK version used for the build. */
    val sdkVersion: String?,
    /** Specific message related to this event, potentially different from `defaultMessage`.
     *  Its exact use depends on Appdome's configuration for the specific event. */
    val message: String?,
    /** Indicates if Appdome's FailSafe enforcement is active for this event. */
    val failSafeEnforce: String?,
    /** External identifier that can be used to correlate this event with external systems. */
    val externalID: String?,
    /** Code representing the reason for the event. */
    val reasonCode: String?,
    /** Date when the application was built. */
    val buildDate: String?,
    /** Platform of the device (e.g., "android"). */
    val devicePlatform: String?,
    /** Name of the network carrier (e.g., "Verizon"). */
    val carrierName: String?,
    /** Updated OS version, if applicable (e.g., after an OTA update). */
    val updatedOSVersion: String?,
    /** Time zone of the device. */
    val timeZone: String?,
    /** Indicates if the device was face down when the event occurred. */
    val deviceFaceDown: String?,
    /** Longitude of the device's location. */
    val locationLong: String?,
    /** Latitude of the device's location. */
    val locationLat: String?,
    /** State or region of the device's location. */
    val locationState: String?,
    /** SSID of the Wi-Fi network the device is connected to. */
    val wifiSsid: String?,
    /** Status of Wi-Fi SSID permission (e.g., if permission is granted to read it). */
    val wifiSsidPermissionStatus: String?,
    /** Specific code identifying the threat. */
    val threatCode: String?,
    val sessionID: String?,
    val fusionSetId: String?,
    val handler: String?,
    val vendor: String?,
    val attackType: String?,
    val threatDescription: String?,
    val remediation: String?,
    val confidentiality: String?,
    val integrity: String?,
    val availability: String?,
    val severity: String?,
    val cvss: String?,
    val cve: String?,
    val cwe: String?,
    val capec: String?,
    val attack: String?,
    val mitre: String?,
    val owasp: String?,
    val nist: String?,
    val pci: String?,
    val hipaa: String?,
    val gdpr: String?,
    val ccpa: String?,
    val pii: String?,
    val phi: String?,
    val spi: String?,
    val finra: String?,
    val sox: String?,
    val fisma: String?,
    val nerc: String?,
    val ferc: String?,
    val cobit: String?,
    val iso: String?,
    val isa: String?,
    val iec: String?,
    val ieee: String?,
    val ics: String?,
    val scada: String?,
    val plc: String?,
    val dcs: String?,
    val rtu: String?,
    val hmi: String?,
    val sis: String?,
    val ews: String?,
    val mes: String?,
    val erp: String?,
    val crm: String?,
    val scm: String?,
    val plm: String?,
    val mom: String?,
    val bom: String?,
    val eam: String?,
    val cmms: String?,
    val fsm: String?,
    val wms: String?,
    val tms: String?,
    val lms: String?,
    val oms: String?,
    val pos: String?,
    val kiosk: String?,
    val atm: String?,
    val vending: String?,
    val gaming: String?,
    val lottery: String?,
    val voting: String?,
    val casino: String?,
    val sports: String?,
    val betting: String?,
    val fantasy: String?,
    val poker: String?,
    val bingo: String?,
    val roulette: String?,
    val blackjack: String?,
    val slots: String?,
    val baccarat: String?,
    val craps: String?,
    val keno: String?,
    val paigow: String?,
    val scratch: String?,
    val wheel: String?,
    val racing: String?,
    val fighting: String?,
    val shooting: String?,
    val strategy: String?,
    val puzzle: String?,
    val adventure: String?,
    val arcade: String?,
    val simulation: String?,
    val roleplaying: String?,
    val educational: String?,
    val family: String?,
    val music: String?,
    val word: String?,
    val trivia: String?,
    val card: String?,
    val board: String?,
    val casual: String?,
    val hypercasual: String?,
    val midcore: String?,
    val hardcore: String?,
    val esports: String?,
    val streaming: String?,
    val vr: String?,
    val ar: String?,
    val mr: String?,
    val xr: String?,
    val metaverse: String?
) : Parcelable
