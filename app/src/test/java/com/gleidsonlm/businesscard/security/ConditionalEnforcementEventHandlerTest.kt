package com.gleidsonlm.businesscard.security

import android.util.Log
import com.gleidsonlm.businesscard.model.ThreatEventData
import io.mockk.clearAllMocks
import io.mockk.every
import io.mockk.mockkStatic
import io.mockk.unmockkStatic
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.Assert.*

/**
 * Unit tests for ConditionalEnforcementEventHandler to verify proper enforcement decisions.
 */
class ConditionalEnforcementEventHandlerTest {

    private lateinit var handler: ConditionalEnforcementEventHandler

    @Before
    fun setup() {
        // Mock Android Log to avoid Robolectric dependency
        mockkStatic(Log::class)
        every { Log.d(any<String>(), any<String>()) } returns 0
        every { Log.w(any<String>(), any<String>()) } returns 0
        every { Log.e(any<String>(), any<String>(), any<Throwable>()) } returns 0
        every { Log.i(any<String>(), any<String>()) } returns 0
        
        handler = ConditionalEnforcementEventHandler()
    }

    @After
    fun tearDown() {
        unmockkStatic(Log::class)
        clearAllMocks()
    }

    @Test
    fun `handleConditionalEnforcementEventEvent should return LOG_ONLY when failSafeEnforce is false`() {
        val threatEventData = createBasicThreatEventData().copy(
            failSafeEnforce = "false"
        )

        val result = handler.handleConditionalEnforcementEventEvent(threatEventData)

        assertEquals(ConditionalEnforcementResult.LOG_ONLY, result)
    }

    @Test
    fun `handleConditionalEnforcementEventEvent should return LOG_ONLY when failSafeEnforce is allow`() {
        val threatEventData = createBasicThreatEventData().copy(
            failSafeEnforce = "allow"
        )

        val result = handler.handleConditionalEnforcementEventEvent(threatEventData)

        assertEquals(ConditionalEnforcementResult.LOG_ONLY, result)
    }

    @Test
    fun `handleConditionalEnforcementEventEvent should return ENFORCE_WITH_NOTIFICATION when failSafeEnforce is true`() {
        val threatEventData = createBasicThreatEventData().copy(
            failSafeEnforce = "true"
        )

        val result = handler.handleConditionalEnforcementEventEvent(threatEventData)

        assertEquals(ConditionalEnforcementResult.ENFORCE_WITH_NOTIFICATION, result)
    }

    @Test
    fun `handleConditionalEnforcementEventEvent should return ENFORCE_WITH_NOTIFICATION when failSafeEnforce is enforce`() {
        val threatEventData = createBasicThreatEventData().copy(
            failSafeEnforce = "enforce"
        )

        val result = handler.handleConditionalEnforcementEventEvent(threatEventData)

        assertEquals(ConditionalEnforcementResult.ENFORCE_WITH_NOTIFICATION, result)
    }

    @Test
    fun `handleConditionalEnforcementEventEvent should return ENFORCE_WITH_NOTIFICATION when reasonCode contains CRITICAL`() {
        val threatEventData = createBasicThreatEventData().copy(
            reasonCode = "CRITICAL_THREAT_DETECTED",
            failSafeEnforce = null
        )

        val result = handler.handleConditionalEnforcementEventEvent(threatEventData)

        assertEquals(ConditionalEnforcementResult.ENFORCE_WITH_NOTIFICATION, result)
    }

    @Test
    fun `handleConditionalEnforcementEventEvent should return LOG_ONLY by default when no explicit enforcement is required`() {
        val threatEventData = createBasicThreatEventData().copy(
            failSafeEnforce = null,
            reasonCode = "standard_detection"
        )

        val result = handler.handleConditionalEnforcementEventEvent(threatEventData)

        assertEquals(ConditionalEnforcementResult.LOG_ONLY, result)
    }

    @Test
    fun `handleConditionalEnforcementEventEvent should handle case insensitive failSafeEnforce values`() {
        val threatEventDataTrue = createBasicThreatEventData().copy(
            failSafeEnforce = "TRUE"
        )
        val threatEventDataFalse = createBasicThreatEventData().copy(
            failSafeEnforce = "FALSE"
        )

        val resultTrue = handler.handleConditionalEnforcementEventEvent(threatEventDataTrue)
        val resultFalse = handler.handleConditionalEnforcementEventEvent(threatEventDataFalse)

        assertEquals(ConditionalEnforcementResult.ENFORCE_WITH_NOTIFICATION, resultTrue)
        assertEquals(ConditionalEnforcementResult.LOG_ONLY, resultFalse)
    }

    @Test
    fun `handleConditionalEnforcementEventEvent should handle case insensitive reasonCode`() {
        val threatEventData = createBasicThreatEventData().copy(
            reasonCode = "critical_security_violation",
            failSafeEnforce = null
        )

        val result = handler.handleConditionalEnforcementEventEvent(threatEventData)

        assertEquals(ConditionalEnforcementResult.ENFORCE_WITH_NOTIFICATION, result)
    }

    private fun createBasicThreatEventData(): ThreatEventData {
        return ThreatEventData(
            id = "test-id",
            eventType = "ConditionalEnforcementEvent",
            receivedTimestamp = System.currentTimeMillis(),
            defaultMessage = "Test conditional enforcement event",
            internalError = null,
            timeStamp = null,
            deviceID = "test-device",
            deviceModel = null,
            osVersion = null,
            kernelInfo = null,
            deviceManufacturer = null,
            fusedAppToken = "test-token",
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
    }
}