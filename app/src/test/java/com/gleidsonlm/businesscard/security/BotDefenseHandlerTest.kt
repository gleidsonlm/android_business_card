package com.gleidsonlm.businesscard.security

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.gleidsonlm.businesscard.model.ThreatEventData
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.mockk
import io.mockk.slot
import io.mockk.verify
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestDispatcher
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

/**
 * Unit tests for [BotDefenseHandler].
 *
 * These tests validate the bot detection logic, response mechanisms,
 * and configuration handling functionality.
 */
@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(RobolectricTestRunner::class)
@Config(sdk = [34])
class BotDefenseHandlerTest {

    private lateinit var context: Context

    @RelaxedMockK
    private lateinit var callback: BotDetectionCallback

    private lateinit var testDispatcher: TestDispatcher
    private lateinit var botDefenseHandler: BotDefenseHandler

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        testDispatcher = StandardTestDispatcher()
        Dispatchers.setMain(testDispatcher)
        context = ApplicationProvider.getApplicationContext()
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    private fun createBotDefenseHandler(config: BotDefenseConfig = BotDefenseConfig()): BotDefenseHandler {
        return BotDefenseHandler(context, config, testDispatcher)
    }

    private fun createThreatEventData(
        threatCode: String? = null,
        deviceModel: String? = null,
        buildHost: String? = null,
        buildUser: String? = null
    ): ThreatEventData {
        return ThreatEventData(
            defaultMessage = "Test threat event",
            internalError = null,
            timeStamp = "2025-01-21T10:30:00Z",
            deviceID = "test-device-123",
            deviceModel = deviceModel,
            osVersion = "14",
            kernelInfo = null,
            deviceManufacturer = "Test Manufacturer",
            fusedAppToken = "test-token",
            carrierPlmn = null,
            deviceBrand = "test",
            deviceBoard = null,
            buildHost = buildHost,
            buildUser = buildUser,
            sdkVersion = "34",
            message = "Test message",
            failSafeEnforce = null,
            externalID = null,
            reasonCode = null,
            buildDate = "2025-01-21",
            devicePlatform = "android",
            carrierName = null,
            updatedOSVersion = null,
            timeZone = "UTC",
            deviceFaceDown = null,
            locationLong = null,
            locationLat = null,
            locationState = null,
            wifiSsid = null,
            wifiSsidPermissionStatus = null,
            threatCode = threatCode
        )
    }

    @Test
    fun `handleBotDetectionEvent with critical threat code triggers critical response`() = runTest {
        // Given
        val config = BotDefenseConfig(
            enableUserNotification = true,
            enableAppProtection = true
        )
        botDefenseHandler = createBotDefenseHandler(config)
        val threatData = createThreatEventData(threatCode = "BOT_CRITICAL_001")

        // When
        botDefenseHandler.handleBotDetectionEvent(threatData, callback)
        testDispatcher.scheduler.advanceUntilIdle() // Allow coroutines to complete
        testDispatcher.scheduler.advanceUntilIdle() // Allow coroutines to complete

        // Then
        verify { callback.onCriticalThreatDetected(threatData) }
        verify { callback.onDetectionComplete(BotResponseAction.APP_PROTECTION) }
    }

    @Test
    fun `handleBotDetectionEvent with high threat code triggers security measures`() = runTest {
        // Given
        val config = BotDefenseConfig(
            enableUserNotification = true,
            enableSecurityCountermeasures = true
        )
        botDefenseHandler = createBotDefenseHandler(config)
        val threatData = createThreatEventData(threatCode = "BOT_HIGH_002")

        // When
        botDefenseHandler.handleBotDetectionEvent(threatData, callback)
        testDispatcher.scheduler.advanceUntilIdle() // Allow coroutines to complete
        testDispatcher.scheduler.advanceUntilIdle() // Allow coroutines to complete

        // Then
        verify { callback.onSecurityCountermeasuresTriggered() }
        verify { callback.onUserNotificationRequired(any()) }
        verify { callback.onDetectionComplete(BotResponseAction.SECURITY_MEASURES) }
    }

    @Test
    fun `handleBotDetectionEvent with medium threat code triggers user warning`() = runTest {
        // Given
        val config = BotDefenseConfig(enableUserNotification = true)
        botDefenseHandler = createBotDefenseHandler(config)
        val threatData = createThreatEventData(threatCode = "BOT_MEDIUM_003")

        val messageSlot = slot<String>()

        // When
        botDefenseHandler.handleBotDetectionEvent(threatData, callback)
        testDispatcher.scheduler.advanceUntilIdle() // Allow coroutines to complete
        testDispatcher.scheduler.advanceUntilIdle() // Allow coroutines to complete

        // Then
        verify { callback.onUserNotificationRequired(capture(messageSlot)) }
        verify { callback.onDetectionComplete(BotResponseAction.WARN_USER) }
        
        assertTrue("Message should contain warning about automated activity", 
            messageSlot.captured.contains("Automated activity detected"))
    }

    @Test
    fun `handleBotDetectionEvent with low threat in log only mode only logs`() = runTest {
        // Given
        val config = BotDefenseConfig(isLogOnlyMode = true)
        botDefenseHandler = createBotDefenseHandler(config)
        val threatData = createThreatEventData(threatCode = "BOT_LOW_004")

        // When
        botDefenseHandler.handleBotDetectionEvent(threatData, callback)
        testDispatcher.scheduler.advanceUntilIdle() // Allow coroutines to complete

        // Then
        verify { callback.onDetectionComplete(BotResponseAction.LOG_ONLY) }
        verify(exactly = 0) { callback.onUserNotificationRequired(any()) }
        verify(exactly = 0) { callback.onSecurityCountermeasuresTriggered() }
    }

    @Test
    fun `handleBotDetectionEvent detects emulator device model`() = runTest {
        // Given
        botDefenseHandler = createBotDefenseHandler()
        val threatData = createThreatEventData(deviceModel = "Android SDK built for x86_64 emulator")

        // When
        botDefenseHandler.handleBotDetectionEvent(threatData, callback)
        testDispatcher.scheduler.advanceUntilIdle() // Allow coroutines to complete

        // Then
        verify { callback.onDetectionComplete(any()) }
        // Bot should be detected due to emulator device model
    }

    @Test
    fun `handleBotDetectionEvent detects development build characteristics`() = runTest {
        // Given
        botDefenseHandler = createBotDefenseHandler()
        val threatData = createThreatEventData(
            buildHost = "build-server.test.com",
            buildUser = "android-build"
        )

        // When
        botDefenseHandler.handleBotDetectionEvent(threatData, callback)
        testDispatcher.scheduler.advanceUntilIdle() // Allow coroutines to complete

        // Then
        verify { callback.onDetectionComplete(any()) }
        // Bot should be detected due to development build characteristics
    }

    @Test
    fun `handleBotDetectionEvent handles exceptions gracefully`() = runTest {
        // Given
        botDefenseHandler = createBotDefenseHandler()
        val mockCallback = mockk<BotDetectionCallback>(relaxed = true)
        every { mockCallback.onDetectionComplete(any()) } throws RuntimeException("Test exception")
        
        val threatData = createThreatEventData()

        // When
        botDefenseHandler.handleBotDetectionEvent(threatData, mockCallback)
        testDispatcher.scheduler.advanceUntilIdle() // Allow coroutines to complete

        // Then
        verify { mockCallback.onError(any()) }
    }

    @Test
    fun `handleBotDetectionEvent with disabled notifications does not notify user`() = runTest {
        // Given
        val config = BotDefenseConfig(
            enableUserNotification = false,
            enableSecurityCountermeasures = true
        )
        botDefenseHandler = createBotDefenseHandler(config)
        val threatData = createThreatEventData(threatCode = "BOT_HIGH_005")

        // When
        botDefenseHandler.handleBotDetectionEvent(threatData, callback)
        testDispatcher.scheduler.advanceUntilIdle() // Allow coroutines to complete

        // Then
        verify { callback.onSecurityCountermeasuresTriggered() }
        verify(exactly = 0) { callback.onUserNotificationRequired(any()) }
        verify { callback.onDetectionComplete(BotResponseAction.SECURITY_MEASURES) }
    }

    @Test
    fun `handleBotDetectionEvent with disabled security measures does not trigger countermeasures`() = runTest {
        // Given
        val config = BotDefenseConfig(
            enableUserNotification = true,
            enableSecurityCountermeasures = false
        )
        botDefenseHandler = createBotDefenseHandler(config)
        val threatData = createThreatEventData(threatCode = "BOT_HIGH_006")

        // When
        botDefenseHandler.handleBotDetectionEvent(threatData, callback)
        testDispatcher.scheduler.advanceUntilIdle() // Allow coroutines to complete

        // Then
        verify { callback.onUserNotificationRequired(any()) }
        verify(exactly = 0) { callback.onSecurityCountermeasuresTriggered() }
        verify { callback.onDetectionComplete(BotResponseAction.SECURITY_MEASURES) }
    }

    @Test
    fun `handleBotDetectionEvent without callback does not crash`() = runTest {
        // Given
        botDefenseHandler = createBotDefenseHandler()
        val threatData = createThreatEventData()

        // When & Then (should not throw exception)
        botDefenseHandler.handleBotDetectionEvent(threatData, null)
        testDispatcher.scheduler.advanceUntilIdle() // Allow coroutines to complete
    }

    @Test
    fun `BotDefenseConfig default values are correct`() {
        // Given
        val config = BotDefenseConfig()

        // Then
        assertEquals(BotSensitivityLevel.MEDIUM, config.sensitivityLevel)
        assertTrue(config.enableUserNotification)
        assertTrue(config.enableSecurityCountermeasures)
        assertFalse(config.enableAppProtection)
        assertFalse(config.isLogOnlyMode)
    }

    @Test
    fun `BotDetectionResult contains expected data`() {
        // Given
        val indicators = listOf("Test indicator 1", "Test indicator 2")
        val timestamp = System.currentTimeMillis()

        // When
        val result = BotDetectionResult(
            isBot = true,
            severity = BotThreatSeverity.HIGH,
            indicators = indicators,
            timestamp = timestamp
        )

        // Then
        assertTrue(result.isBot)
        assertEquals(BotThreatSeverity.HIGH, result.severity)
        assertEquals(2, result.indicators.size)
        assertEquals(timestamp, result.timestamp)
    }
}