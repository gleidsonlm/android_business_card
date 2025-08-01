package com.gleidsonlm.businesscard

import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.test.core.app.ApplicationProvider
import com.gleidsonlm.businesscard.data.repository.ThreatEventRepository
import com.gleidsonlm.businesscard.model.ThreatEventData
import com.gleidsonlm.businesscard.security.BotDefenseHandler
import com.gleidsonlm.businesscard.security.BotDetectionCallback
import com.gleidsonlm.businesscard.security.BotResponseAction
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.mockk
import io.mockk.slot
import io.mockk.verify
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

/**
 * Unit tests for [ThreatEventReceiver] integration with [BotDefenseHandler].
 *
 * These tests validate the proper handling of MobileBotDefenseCheck events
 * and integration between the receiver and bot defense handler.
 */
@RunWith(RobolectricTestRunner::class)
@Config(sdk = [34])
class ThreatEventReceiverTest {

    @MockK
    private lateinit var mockContext: Context
    
    private lateinit var context: Context

    @RelaxedMockK
    private lateinit var botDefenseHandler: BotDefenseHandler
    
    @RelaxedMockK
    private lateinit var threatEventRepository: ThreatEventRepository

    private lateinit var threatEventReceiver: ThreatEventReceiver

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        
        // Get real context and mock context for verification
        context = ApplicationProvider.getApplicationContext()
        
        // Mock context.startActivity to avoid ActivityNotFoundException in tests
        every { mockContext.startActivity(any()) } returns Unit
        every { mockContext.packageName } returns "com.gleidsonlm.businesscard"
        
        threatEventReceiver = ThreatEventReceiver(mockContext, threatEventRepository)
        threatEventReceiver.setBotDefenseHandler(botDefenseHandler)
    }

    private fun createMobileBotDefenseCheckIntent(): Intent {
        return Intent("MobileBotDefenseCheck").apply {
            putExtra("defaultMessage", "Bot activity detected")
            putExtra("threatCode", "BOT_HIGH_001")
            putExtra("deviceModel", "Test Device")
            putExtra("timeStamp", "2025-01-21T10:30:00Z")
            putExtra("deviceID", "test-device-123")
            putExtra("osVersion", "14")
            putExtra("message", "Automated behavior detected")
        }
    }

    private fun createNonBotThreatIntent(): Intent {
        return Intent("RootedDevice").apply {
            putExtra("defaultMessage", "Rooted device detected")
            putExtra("threatCode", "ROOT_001")
            putExtra("deviceModel", "Test Device")
        }
    }

    @Test
    fun `onEvent with MobileBotDefenseCheck calls bot defense handler`() {
        // Given
        val intent = createMobileBotDefenseCheckIntent()
        val threatDataSlot = slot<ThreatEventData>()
        val callbackSlot = slot<BotDetectionCallback>()

        // When
        threatEventReceiver.onEvent(intent)

        // Then
        verify { 
            botDefenseHandler.handleBotDetectionEvent(
                capture(threatDataSlot), 
                capture(callbackSlot)
            ) 
        }
        
        val capturedThreatData = threatDataSlot.captured
        assertEquals("Bot activity detected", capturedThreatData.defaultMessage)
        assertEquals("BOT_HIGH_001", capturedThreatData.threatCode)
        assertEquals("Automated behavior detected", capturedThreatData.message)
    }

    @Test
    fun `onEvent with non-bot threat does not automatically navigate to activity`() {
        // Given
        val intent = createNonBotThreatIntent()

        // When
        threatEventReceiver.onEvent(intent)

        // Then
        verify(exactly = 0) { botDefenseHandler.handleBotDetectionEvent(any(), any()) }
        // Should NOT automatically start activity - events are saved for list view
        verify(exactly = 0) { mockContext.startActivity(any()) }
        // Should save the event to repository for list display
        verify { threatEventRepository.saveEvent(any()) }
    }

    @Test
    fun `bot detection callback onDetectionComplete with LOG_ONLY does not start activity`() {
        // Given
        val intent = createMobileBotDefenseCheckIntent()
        val callbackSlot = slot<BotDetectionCallback>()
        
        every { 
            botDefenseHandler.handleBotDetectionEvent(any(), capture(callbackSlot)) 
        } answers {
            // Simulate immediate callback execution
            callbackSlot.captured.onDetectionComplete(BotResponseAction.LOG_ONLY)
        }

        // When
        threatEventReceiver.onEvent(intent)

        // Then
        // Should not start ThreatEventActivity for LOG_ONLY action
        verify(exactly = 0) { mockContext.startActivity(any()) }
    }

    @Test
    fun `bot detection callback onDetectionComplete with MONITOR does not start activity`() {
        // Given
        val intent = createMobileBotDefenseCheckIntent()
        val callbackSlot = slot<BotDetectionCallback>()
        
        every { 
            botDefenseHandler.handleBotDetectionEvent(any(), capture(callbackSlot)) 
        } answers {
            // Simulate immediate callback execution
            callbackSlot.captured.onDetectionComplete(BotResponseAction.MONITOR)
        }

        // When
        threatEventReceiver.onEvent(intent)

        // Then
        // Should not start ThreatEventActivity for MONITOR action - event saved to list
        verify(exactly = 0) { mockContext.startActivity(any()) }
    }

    @Test
    fun `bot detection callback onDetectionComplete with SECURITY_MEASURES starts activity`() {
        // Given
        val intent = createMobileBotDefenseCheckIntent()
        val callbackSlot = slot<BotDetectionCallback>()
        
        every { 
            botDefenseHandler.handleBotDetectionEvent(any(), capture(callbackSlot)) 
        } answers {
            // Simulate immediate callback execution
            callbackSlot.captured.onDetectionComplete(BotResponseAction.SECURITY_MEASURES)
        }

        // When
        threatEventReceiver.onEvent(intent)

        // Then
        verify { mockContext.startActivity(any()) }
    }

    @Test
    fun `bot detection callback onDetectionComplete with APP_PROTECTION starts activity`() {
        // Given
        val intent = createMobileBotDefenseCheckIntent()
        val callbackSlot = slot<BotDetectionCallback>()
        
        every { 
            botDefenseHandler.handleBotDetectionEvent(any(), capture(callbackSlot)) 
        } answers {
            // Simulate immediate callback execution
            callbackSlot.captured.onDetectionComplete(BotResponseAction.APP_PROTECTION)
        }

        // When
        threatEventReceiver.onEvent(intent)

        // Then
        verify { mockContext.startActivity(any()) }
    }

    @Test
    fun `bot detection callback onUserNotificationRequired starts activity with enhanced message`() {
        // Given
        val intent = createMobileBotDefenseCheckIntent()
        val callbackSlot = slot<BotDetectionCallback>()
        val activityIntentSlot = slot<Intent>()
        val testMessage = "Custom bot detection message"
        
        every { 
            botDefenseHandler.handleBotDetectionEvent(any(), capture(callbackSlot)) 
        } answers {
            // Simulate immediate callback execution
            callbackSlot.captured.onUserNotificationRequired(testMessage)
        }
        
        every { mockContext.startActivity(capture(activityIntentSlot)) } returns Unit

        // When
        threatEventReceiver.onEvent(intent)

        // Then
        verify { mockContext.startActivity(any()) }
        
        val capturedIntent = activityIntentSlot.captured
        val threatData = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            capturedIntent.getParcelableExtra(
                ThreatEventActivity.EXTRA_THREAT_EVENT_DATA,
                ThreatEventData::class.java
            )
        } else {
            @Suppress("DEPRECATION")
            capturedIntent.getParcelableExtra<ThreatEventData>(
                ThreatEventActivity.EXTRA_THREAT_EVENT_DATA
            )
        }
        
        assertNotNull(threatData)
        assertEquals(testMessage, threatData?.message)
        assertEquals(testMessage, threatData?.defaultMessage)
    }

    @Test
    fun `bot detection callback onCriticalThreatDetected starts activity with critical message`() {
        // Given
        val intent = createMobileBotDefenseCheckIntent()
        val callbackSlot = slot<BotDetectionCallback>()
        val activityIntentSlot = slot<Intent>()
        
        every { 
            botDefenseHandler.handleBotDetectionEvent(any(), capture(callbackSlot)) 
        } answers {
            val threatData = firstArg<ThreatEventData>()
            callbackSlot.captured.onCriticalThreatDetected(threatData)
        }
        
        every { mockContext.startActivity(capture(activityIntentSlot)) } returns Unit

        // When
        threatEventReceiver.onEvent(intent)

        // Then
        verify { mockContext.startActivity(any()) }
        
        val capturedIntent = activityIntentSlot.captured
        val threatData = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            capturedIntent.getParcelableExtra(
                ThreatEventActivity.EXTRA_THREAT_EVENT_DATA,
                ThreatEventData::class.java
            )
        } else {
            @Suppress("DEPRECATION")
            capturedIntent.getParcelableExtra<ThreatEventData>(
                ThreatEventActivity.EXTRA_THREAT_EVENT_DATA
            )
        }
        
        assertNotNull(threatData)
        assertTrue("Should contain critical message", 
            threatData?.message?.contains("CRITICAL") == true)
    }

    @Test
    fun `bot detection callback onError does not start activity - saves to list`() {
        // Given
        val intent = createMobileBotDefenseCheckIntent()
        val callbackSlot = slot<BotDetectionCallback>()
        val testException = RuntimeException("Test bot detection error")
        
        every { 
            botDefenseHandler.handleBotDetectionEvent(any(), capture(callbackSlot)) 
        } answers {
            callbackSlot.captured.onError(testException)
        }

        // When
        threatEventReceiver.onEvent(intent)

        // Then
        // Should NOT start activity for errors - just save to list for review
        verify(exactly = 0) { mockContext.startActivity(any()) }
        verify { threatEventRepository.saveEvent(any()) }
    }

    @Test
    fun `onEvent without bot defense handler saves to list only`() {
        // Given
        val receiverWithoutHandler = ThreatEventReceiver(mockContext, threatEventRepository)
        val intent = createMobileBotDefenseCheckIntent()

        // When
        receiverWithoutHandler.onEvent(intent)

        // Then
        // Should NOT start ThreatEventActivity - just save to list
        verify(exactly = 0) { mockContext.startActivity(any()) }
        verify { threatEventRepository.saveEvent(any()) }
    }

    @Test
    fun `onEvent with null action returns early`() {
        // Given
        val intent = Intent() // Intent without action

        // When
        threatEventReceiver.onEvent(intent)

        // Then
        verify(exactly = 0) { botDefenseHandler.handleBotDetectionEvent(any(), any()) }
        verify(exactly = 0) { mockContext.startActivity(any()) }
    }

    @Test
    fun `register method registers MobileBotDefenseCheck filter`() {
        // This test would require more complex mocking of the registration process
        // For now, we verify that the method doesn't crash
        
        // Given & When
        threatEventReceiver.register()
        
        // Then - no exception should be thrown
        // In a real test environment, we would verify the IntentFilter registration
    }

    @Test
    fun `onEvent handles DetectUnlockedBootloader threat event without auto-navigation`() {
        // Given
        val intent = Intent("DetectUnlockedBootloader").apply {
            putExtra("defaultMessage", "Unlocked bootloader detected")
            putExtra("deviceID", "test-device-123")
            putExtra("threatCode", "UNLOCKED_BOOTLOADER")
        }
        val handlerFunction = mockk<(ThreatEventData) -> Unit>(relaxed = true)
        threatEventReceiver.addHandler("DetectUnlockedBootloader", handlerFunction)

        // When
        threatEventReceiver.onEvent(intent)

        // Then
        verify { handlerFunction.invoke(any()) }
        // Should NOT automatically start activity - events saved to list
        verify(exactly = 0) { mockContext.startActivity(any()) }
        verify { threatEventRepository.saveEvent(any()) }
    }

    @Test
    fun `onEvent handles FridaDetected threat event without auto-navigation`() {
        // Given
        val intent = Intent("FridaDetected").apply {
            putExtra("defaultMessage", "Frida instrumentation detected")
            putExtra("deviceID", "test-device-456")
            putExtra("threatCode", "FRIDA_DETECTED")
        }
        val handlerFunction = mockk<(ThreatEventData) -> Unit>(relaxed = true)
        threatEventReceiver.addHandler("FridaDetected", handlerFunction)

        // When
        threatEventReceiver.onEvent(intent)

        // Then
        verify { handlerFunction.invoke(any()) }
        // Should NOT automatically start activity - events saved to list
        verify(exactly = 0) { mockContext.startActivity(any()) }
        verify { threatEventRepository.saveEvent(any()) }
    }

    @Test
    fun `onEvent handles MalwareInjectionDetected threat event without auto-navigation`() {
        // Given
        val intent = Intent("MalwareInjectionDetected").apply {
            putExtra("defaultMessage", "Malware injection detected")
            putExtra("deviceID", "test-device-789")
            putExtra("threatCode", "MALWARE_INJECTION")
        }
        val handlerFunction = mockk<(ThreatEventData) -> Unit>(relaxed = true)
        threatEventReceiver.addHandler("MalwareInjectionDetected", handlerFunction)

        // When
        threatEventReceiver.onEvent(intent)

        // Then
        verify { handlerFunction.invoke(any()) }
        // Should NOT automatically start activity - events saved to list
        verify(exactly = 0) { mockContext.startActivity(any()) }
        verify { threatEventRepository.saveEvent(any()) }
    }
}