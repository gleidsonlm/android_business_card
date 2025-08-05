package com.gleidsonlm.businesscard.security

import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import java.io.ByteArrayOutputStream
import java.io.PrintStream

/**
 * Unit tests for NativeLibraryProtection to ensure proper error handling
 * and crash prevention for Appdome's libloader.so RSA_size function failures.
 */
@RunWith(RobolectricTestRunner::class)
@Config(sdk = [28])
class NativeLibraryProtectionTest {

    private lateinit var originalOut: PrintStream
    private lateinit var testOut: ByteArrayOutputStream

    @Before
    fun setUp() {
        // Capture system output for testing
        originalOut = System.out
        testOut = ByteArrayOutputStream()
        System.setOut(PrintStream(testOut))
    }

    @Test
    fun `safeNativeOperation should execute block successfully`() {
        // Given
        val expectedResult = "test_result"
        
        // When
        val result = NativeLibraryProtection.safeNativeOperation(
            operation = "test operation"
        ) {
            expectedResult
        }
        
        // Then
        assertEquals(expectedResult, result)
    }

    @Test
    fun `safeNativeOperation should handle UnsatisfiedLinkError`() {
        // Given
        val fallbackResult = "fallback_result"
        
        // When
        val result = NativeLibraryProtection.safeNativeOperation(
            operation = "test operation",
            fallback = { fallbackResult }
        ) {
            throw UnsatisfiedLinkError("Native library not found")
        }
        
        // Then
        assertEquals(fallbackResult, result)
    }

    @Test
    fun `safeNativeOperation should handle SecurityException`() {
        // Given
        val fallbackResult = "fallback_result"
        
        // When
        val result = NativeLibraryProtection.safeNativeOperation(
            operation = "test operation",
            fallback = { fallbackResult }
        ) {
            throw SecurityException("Security violation")
        }
        
        // Then
        assertEquals(fallbackResult, result)
    }

    @Test
    fun `safeNativeOperation should handle SIGSEGV RuntimeException`() {
        // Given
        val fallbackResult = "fallback_result"
        
        // When
        val result = NativeLibraryProtection.safeNativeOperation(
            operation = "test operation",
            fallback = { fallbackResult }
        ) {
            throw RuntimeException("SIGSEGV in RSA_size function")
        }
        
        // Then
        assertEquals(fallbackResult, result)
    }

    @Test
    fun `safeNativeOperation should handle libloader Error`() {
        // Given
        val fallbackResult = "fallback_result"
        
        // When
        val result = NativeLibraryProtection.safeNativeOperation(
            operation = "test operation",
            fallback = { fallbackResult }
        ) {
            throw Error("Fatal error in libloader.so")
        }
        
        // Then
        assertEquals(fallbackResult, result)
    }

    @Test
    fun `safeNativeOperation should rethrow non-native RuntimeExceptions`() {
        // Given/When/Then
        assertThrows(RuntimeException::class.java) {
            NativeLibraryProtection.safeNativeOperation(
                operation = "test operation"
            ) {
                throw RuntimeException("Regular runtime exception")
            }
        }
    }

    @Test
    fun `validateNativeParameters should return true for valid parameters`() {
        // Given
        val validParams = mapOf(
            "deviceId" to "test_device",
            "token" to "valid_token",
            "data" to byteArrayOf(1, 2, 3)
        )
        
        // When
        val result = NativeLibraryProtection.validateNativeParameters(validParams)
        
        // Then
        assertTrue(result)
    }

    @Test
    fun `validateNativeParameters should return false for null parameters`() {
        // Given
        val invalidParams = mapOf(
            "deviceId" to null,
            "token" to "valid_token"
        )
        
        // When
        val result = NativeLibraryProtection.validateNativeParameters(invalidParams)
        
        // Then
        assertFalse(result)
    }

    @Test
    fun `validateNativeParameters should return false for empty string parameters`() {
        // Given
        val invalidParams = mapOf(
            "deviceId" to "",
            "token" to "valid_token"
        )
        
        // When
        val result = NativeLibraryProtection.validateNativeParameters(invalidParams)
        
        // Then
        assertFalse(result)
    }

    @Test
    fun `validateNativeParameters should return false for empty byte array parameters`() {
        // Given
        val invalidParams = mapOf(
            "deviceId" to "test_device",
            "data" to byteArrayOf()
        )
        
        // When
        val result = NativeLibraryProtection.validateNativeParameters(invalidParams)
        
        // Then
        assertFalse(result)
    }

    @Test
    fun `safeBotDefenseOperation should return true on success`() {
        // Given
        var executionCount = 0
        
        // When
        val result = NativeLibraryProtection.safeBotDefenseOperation("test bot defense") {
            executionCount++
        }
        
        // Then
        assertTrue(result)
        assertEquals(1, executionCount)
    }

    @Test
    fun `safeBotDefenseOperation should return false on failure`() {
        // When
        val result = NativeLibraryProtection.safeBotDefenseOperation("test bot defense") {
            throw RuntimeException("SIGSEGV in mobilebot_defense")
        }
        
        // Then
        assertFalse(result)
    }

    @Test
    fun `initialize should set up uncaught exception handler`() {
        // Given
        val originalHandler = Thread.getDefaultUncaughtExceptionHandler()
        
        // When
        NativeLibraryProtection.initialize()
        
        // Then
        val newHandler = Thread.getDefaultUncaughtExceptionHandler()
        assertNotEquals(originalHandler, newHandler)
        
        // Cleanup
        Thread.setDefaultUncaughtExceptionHandler(originalHandler)
    }

    @Test
    fun `validateNativeParameters should handle exceptions gracefully`() {
        // Given
        val paramsWithException = object : Map<String, Any?> {
            override val entries: Set<Map.Entry<String, Any?>>
                get() = throw RuntimeException("Test exception")
            override val keys: Set<String>
                get() = throw RuntimeException("Test exception")
            override val size: Int
                get() = throw RuntimeException("Test exception")
            override val values: Collection<Any?>
                get() = throw RuntimeException("Test exception")
            override fun containsKey(key: String): Boolean = throw RuntimeException("Test exception")
            override fun containsValue(value: Any?): Boolean = throw RuntimeException("Test exception")
            override fun get(key: String): Any? = throw RuntimeException("Test exception")
            override fun isEmpty(): Boolean = throw RuntimeException("Test exception")
        }
        
        // When
        val result = NativeLibraryProtection.validateNativeParameters(paramsWithException)
        
        // Then
        assertFalse(result)
    }
}