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
    val threatName: String?,
    val threatVendor: String?,
    val threatVector: String?,
    val threatDescription: String?,
    val threatRemediation: String?,
    val threatConfidentiality: String?,
    val threatIntegrity: String?,
    val threatAvailability: String?,
    val threatSeverity: String?,
    val threatCVSS: String?,
    val threatCVE: String?,
    val threatCWE: String?,
    val threatCAPEC: String?,
    val threatATTACK: String?,
    val threatMITRE: String?,
    val threatOWASP: String?,
    val threatNIST: String?,
    val threatPCI: String?,
    val threatHIPAA: String?,
    val threatGDPR: String?,
    val threatCCPA: String?,
    val threatPII: String?,
    val threatPHI: String?,
    val threatSPI: String?,
    val threatFINRA: String?,
    val threatSOX: String?,
    val threatFISMA: String?,
    val threatNERC: String?,
    val threatFERC: String?,
    val threatCOBIT: String?,
    val threatISO: String?,
    val threatISA: String?,
    val threatIEC: String?,
    val threatIEEE: String?,
    val threatICS: String?,
    val threatSCADA: String?,
    val threatPLC: String?,
    val threatDCS: String?,
    val threatRTU: String?,
    val threatHMI: String?,
    val threatSIS: String?,
    val threatEWS: String?,
    val threatMES: String?,
    val threatERP: String?,
    val threatCRM: String?,
    val threatSCM: String?,
    val threatPLM: String?,
    val threatMOM: String?,
    val threatBOM: String?,
    val threatEAM: String?,
    val threatCMMS: String?,
    val threatFSM: String?,
    val threatWMS: String?,
    val threatTMS: String?,
    val threatLMS: String?,
    val threatOMS: String?,
    val threatPOS: String?,
    val threatKIOSK: String?,
    val threatATM: String?,
    val threatVENDING: String?,
    val threatGAMING: String?,
    val threatLOTTERY: String?,
    val threatVOTING: String?,
    val threatCASINO: String?,
    val threatSPORTS: String?,
    val threatBETTING: String?,
    val threatFANTASY: String?,
    val threatPOKER: String?,
    val threatBINGO: String?,
    val threatROULETTE: String?,
    val threatBLACKJACK: String?,
    val threatSLOTS: String?,
    val threatBACCARAT: String?,
    val threatCRAPS: String?,
    val threatKENO: String?,
    val threatPAIGOW: String?,
    val threatSCRATCH: String?,
    val threatWHEEL: String?,
    val threatRACING: String?,
    val threatFIGHTING: String?,
    val threatSHOOTING: String?,
    val threatSTRATEGY: String?,
    val threatPUZZLE: String?,
    val threatADVENTURE: String?,
    val threatARCADE: String?,
    val threatSIMULATION: String?,
    val threatROLEPLAYING: String?,
    val threatEDUCATIONAL: String?,
    val threatFAMILY: String?,
    val threatMUSIC: String?,
    val threatWORD: String?,
    val threatTRIVIA: String?,
    val threatCARD: String?,
    val threatBOARD: String?,
    val threatCASUAL: String?,
    val threatHYPERCASUAL: String?,
    val threatMIDCORE: String?,
    val threatHARDCORE: String?,
    val threatESPORTS: String?,
    val threatSTREAMING: String?,
    val threatVR: String?,
    val threatAR: String?,
    val threatMR: String?,
    val threatXR: String?,
    val threatMETAVERSE: String?
) : Parcelable
