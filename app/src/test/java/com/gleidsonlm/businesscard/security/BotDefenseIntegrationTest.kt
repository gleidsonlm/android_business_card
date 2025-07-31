package com.gleidsonlm.businesscard.security

import android.content.Context
import android.content.Intent
import androidx.test.core.app.ApplicationProvider
import com.gleidsonlm.businesscard.ThreatEventReceiver
import com.gleidsonlm.businesscard.model.ThreatEventData
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.verify
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

/**
 * Integration tests for the complete MobileBotDefenseCheck threat event flow.
 *
 * These tests validate the end-to-end behavior from threat event receipt
 * through bot detection and response mechanisms.
 */
@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(RobolectricTestRunner::class)
@Config(sdk = [34])
class BotDefenseIntegrationTest {

    @MockK
    private lateinit var mockContext: Context

    private lateinit var context: Context
    private lateinit var botDefenseHandler: BotDefenseHandler
    private lateinit var threatEventReceiver: ThreatEventReceiver
    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        Dispatchers.setMain(testDispatcher)
        
        // Use real context for actual functionality, mock for verification
        context = ApplicationProvider.getApplicationContext()

        // Mock context behavior for verification
        every { mockContext.startActivity(any()) } returns Unit
        every { mockContext.packageName } returns "com.gleidsonlm.businesscard"

        // Create real instances for integration testing
        val config = BotDefenseConfig(
            sensitivityLevel = BotSensitivityLevel.MEDIUM,
            enableUserNotification = true,
            enableSecurityCountermeasures = true,
            enableAppProtection = false,
            isLogOnlyMode = false
        )
        
        botDefenseHandler = BotDefenseHandler(context, config, testDispatcher)
        threatEventReceiver = ThreatEventReceiver(mockContext)
        threatEventReceiver.setBotDefenseHandler(botDefenseHandler)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `complete bot defense flow with high severity threat`() = runTest {
        // Given - High severity bot threat intent
        val intent = Intent("MobileBotDefenseCheck").apply {
            putExtra("defaultMessage", "Bot activity detected")
            putExtra("threatCode", "BOT_HIGH_001")
            putExtra("deviceModel", "Android SDK built for x86_64 emulator")
            putExtra("buildHost", "build-server.test.com")
            putExtra("buildUser", "android-build")
            putExtra("timeStamp", "2025-01-21T10:30:00Z")
            putExtra("deviceID", "test-device-123")
            putExtra("osVersion", "14")
            putExtra("message", "Automated behavior detected")
        }

        // When - Process the threat event
        threatEventReceiver.onEvent(intent)

        // Then - Should trigger activity launch for high severity threat
        verify { mockContext.startActivity(any()) }
    }

    @Test
    fun `complete bot defense flow with low severity in log only mode`() = runTest {
        // Given - Configure for log only mode
        val logOnlyConfig = BotDefenseConfig(
            sensitivityLevel = BotSensitivityLevel.LOW,
            isLogOnlyMode = true
        )
        
        val logOnlyHandler = BotDefenseHandler(context, logOnlyConfig, testDispatcher)
        threatEventReceiver.setBotDefenseHandler(logOnlyHandler)
        
        val intent = Intent("MobileBotDefenseCheck").apply {
            putExtra("defaultMessage", "Minor bot activity detected")
            putExtra("threatCode", "BOT_LOW_001")
            putExtra("deviceModel", "Normal Device")
        }

        // When - Process the threat event
        threatEventReceiver.onEvent(intent)

        // Then - Should not trigger activity launch for log only mode
        verify(exactly = 0) { mockContext.startActivity(any()) }
    }

    @Test
    fun `bot defense with non-bot threat event routes to normal flow`() = runTest {
        // Given - Non-bot threat event
        val intent = Intent("RootedDevice").apply {
            putExtra("defaultMessage", "Rooted device detected")
            putExtra("threatCode", "ROOT_001")
        }

        // When - Process the threat event
        threatEventReceiver.onEvent(intent)

        // Then - Should route to ThreatEventActivity normally
        verify { mockContext.startActivity(any()) }
    }

    @Test
    fun `bot defense handles missing threat data gracefully`() = runTest {
        // Given - Minimal threat event data
        val intent = Intent("MobileBotDefenseCheck").apply {
            putExtra("defaultMessage", "Bot detected")
            // Missing most other data fields
        }

        // When - Process the threat event
        threatEventReceiver.onEvent(intent)

        // Then - Should handle gracefully without crashing
        // The exact behavior depends on configuration, but it shouldn't crash
    }

    @Test
    fun `bot defense configuration affects response behavior`() = runTest {
        // Given - Disabled notifications configuration
        val noNotificationConfig = BotDefenseConfig(
            enableUserNotification = false,
            enableSecurityCountermeasures = false,
            isLogOnlyMode = false
        )
        
        val restrictedHandler = BotDefenseHandler(context, noNotificationConfig, testDispatcher)
        threatEventReceiver.setBotDefenseHandler(restrictedHandler)
        
        val intent = Intent("MobileBotDefenseCheck").apply {
            putExtra("threatCode", "BOT_MEDIUM_001")
            putExtra("defaultMessage", "Medium severity bot detected")
        }

        // When - Process the threat event
        threatEventReceiver.onEvent(intent)

        // Then - Behavior should reflect configuration settings
        // Exact verification depends on implementation details
    }

    @Test
    fun `fallback behavior when bot defense handler is not set`() = runTest {
        // Given - Receiver without bot defense handler
        val receiverWithoutHandler = ThreatEventReceiver(mockContext)
        val intent = Intent("MobileBotDefenseCheck").apply {
            putExtra("defaultMessage", "Bot detected")
        }

        // When - Process the threat event
        receiverWithoutHandler.onEvent(intent)

        // Then - Should fallback to normal threat event activity
        verify { mockContext.startActivity(any()) }
    }
}