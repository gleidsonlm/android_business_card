package com.gleidsonlm.businesscard.security

import com.gleidsonlm.businesscard.model.ThreatEventData
import org.junit.Before
import org.junit.Test

/**
 * Unit tests for [DetectUnlockedBootloaderHandler].
 */
class DetectUnlockedBootloaderHandlerTest {

    private lateinit var handler: DetectUnlockedBootloaderHandler

    @Before
    fun setUp() {
        handler = DetectUnlockedBootloaderHandler()
    }

    @Test
    fun `handleDetectUnlockedBootloaderEvent should process threat event without throwing exception`() {
        // Given
        val threatEventData = ThreatEventData(
            deviceID = "test-device-123",
            threatCode = "UNLOCKED_BOOTLOADER",
            defaultMessage = "Unlocked bootloader detected",
            internalError = null,
            timeStamp = "2023-01-01T00:00:00Z",
            deviceModel = "TestDevice",
            osVersion = "13",
            kernelInfo = "test-kernel",
            deviceManufacturer = "TestManufacturer",
            fusedAppToken = "test-token",
            carrierPlmn = "test-plmn",
            deviceBrand = "test-brand",
            deviceBoard = "test-board",
            buildHost = "test-host",
            buildUser = "test-user",
            sdkVersion = "33",
            message = "Test message",
            failSafeEnforce = "true",
            externalID = "test-external-id",
            reasonCode = "001",
            buildDate = "2023-01-01",
            devicePlatform = "android",
            carrierName = "TestCarrier",
            updatedOSVersion = "13.1",
            timeZone = "UTC",
            deviceFaceDown = "false",
            locationLong = "0.0",
            locationLat = "0.0",
            locationState = "TestState",
            wifiSsid = "test-wifi",
            wifiSsidPermissionStatus = "granted"
        )

        // When & Then - should not throw exception
        handler.handleDetectUnlockedBootloaderEvent(threatEventData)
    }

    @Test
    fun `handleDetectUnlockedBootloaderEvent should handle null threat data gracefully`() {
        // Given
        val threatEventData = ThreatEventData(
            deviceID = null,
            threatCode = null,
            defaultMessage = null,
            internalError = null,
            timeStamp = null,
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
            wifiSsidPermissionStatus = null
        )

        // When & Then - should not throw exception
        handler.handleDetectUnlockedBootloaderEvent(threatEventData)
    }
}