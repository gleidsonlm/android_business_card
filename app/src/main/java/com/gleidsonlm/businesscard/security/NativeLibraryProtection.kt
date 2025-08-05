package com.gleidsonlm.businesscard.security

import android.util.Log
import kotlin.coroutines.cancellation.CancellationException

/**
 * Provides protection against native library crashes, specifically for Appdome's libloader.so
 * and RSA_size function failures that can cause SIGSEGV crashes.
 *
 * This utility class implements defensive programming patterns to handle native library
 * failures gracefully without crashing the application.
 */
object NativeLibraryProtection {

    private const val TAG = "NativeLibraryProtection"

    /**
     * Executes a block of code that may interact with native libraries,
     * providing crash protection and graceful error handling.
     *
     * @param operation Description of the operation for logging
     * @param block The code block to execute safely
     * @param fallback Optional fallback action if the operation fails
     * @return Result of the operation or null if it failed
     */
    fun <T> safeNativeOperation(
        operation: String,
        fallback: (() -> T)? = null,
        block: () -> T
    ): T? {
        return try {
            Log.d(TAG, "Starting safe native operation: $operation")
            block()
        } catch (e: UnsatisfiedLinkError) {
            Log.e(TAG, "Native library error during $operation: ${e.message}", e)
            fallback?.invoke()
        } catch (e: SecurityException) {
            Log.e(TAG, "Security exception during $operation: ${e.message}", e)
            fallback?.invoke()
        } catch (e: RuntimeException) {
            // Catch RuntimeExceptions that might wrap native crashes
            if (e.message?.contains("SIGSEGV") == true || 
                e.message?.contains("RSA_size") == true ||
                e.message?.contains("libloader") == true) {
                Log.e(TAG, "Native library crash detected during $operation: ${e.message}", e)
                fallback?.invoke()
            } else {
                throw e // Re-throw if not a native library issue
            }
        } catch (e: Error) {
            // Catch JVM errors that might be caused by native crashes
            if (e.message?.contains("SIGSEGV") == true || 
                e.message?.contains("RSA_size") == true ||
                e.message?.contains("libloader") == true) {
                Log.e(TAG, "Fatal native library error during $operation: ${e.message}", e)
                fallback?.invoke()
            } else {
                throw e // Re-throw if not a native library issue
            }
        } catch (e: CancellationException) {
            // Don't catch cancellation exceptions
            throw e
        } catch (e: Exception) {
            Log.e(TAG, "Unexpected error during $operation: ${e.message}", e)
            fallback?.invoke()
        }
    }

    /**
     * Validates parameters before passing them to native code that might call RSA_size.
     * Helps prevent null pointer dereference crashes in native libraries.
     *
     * @param params Map of parameter names to values
     * @return true if all parameters are valid, false otherwise
     */
    fun validateNativeParameters(params: Map<String, Any?>): Boolean {
        return try {
            val invalidParams = params.filterValues { it == null }
            if (invalidParams.isNotEmpty()) {
                Log.w(TAG, "Null parameters detected before native operation: ${invalidParams.keys}")
                return false
            }
            
            // Additional validation for cryptographic operations
            params.values.forEach { value ->
                when (value) {
                    is String -> if (value.isBlank()) {
                        Log.w(TAG, "Empty string parameter detected")
                        return false
                    }
                    is ByteArray -> if (value.isEmpty()) {
                        Log.w(TAG, "Empty byte array parameter detected")
                        return false
                    }
                }
            }
            
            true
        } catch (e: Exception) {
            Log.e(TAG, "Error validating native parameters", e)
            false
        }
    }

    /**
     * Creates a safe wrapper for suspend operations that might trigger native library crashes.
     *
     * @param operation Description of the operation for logging
     * @param block The suspend operation to execute safely
     * @return true if successful, false if failed
     */
    suspend fun safeSuspendOperation(
        operation: String,
        block: suspend () -> Unit
    ): Boolean {
        return try {
            Log.d(TAG, "Starting safe suspend operation: $operation")
            block()
            true
        } catch (e: UnsatisfiedLinkError) {
            Log.e(TAG, "Native library error during $operation: ${e.message}", e)
            false
        } catch (e: SecurityException) {
            Log.e(TAG, "Security exception during $operation: ${e.message}", e)
            false
        } catch (e: RuntimeException) {
            // Catch RuntimeExceptions that might wrap native crashes
            if (e.message?.contains("SIGSEGV") == true || 
                e.message?.contains("RSA_size") == true ||
                e.message?.contains("libloader") == true) {
                Log.e(TAG, "Native library crash detected during $operation: ${e.message}", e)
                false
            } else {
                throw e // Re-throw if not a native library issue
            }
        } catch (e: Error) {
            // Catch JVM errors that might be caused by native crashes
            if (e.message?.contains("SIGSEGV") == true || 
                e.message?.contains("RSA_size") == true ||
                e.message?.contains("libloader") == true) {
                Log.e(TAG, "Fatal native library error during $operation: ${e.message}", e)
                false
            } else {
                throw e // Re-throw if not a native library issue
            }
        } catch (e: CancellationException) {
            // Don't catch cancellation exceptions
            throw e
        } catch (e: Exception) {
            Log.e(TAG, "Unexpected error during $operation: ${e.message}", e)
            false
        }
    }

    /**
     * Creates a safe wrapper for operations that might trigger Appdome's mobile bot defense
     * heartbeat operations, which are prone to RSA_size null pointer crashes.
     *
     * @param operation Description of the bot defense operation
     * @param block The bot defense operation to execute
     * @return true if successful, false if failed
     */
    fun safeBotDefenseOperation(
        operation: String,
        block: () -> Unit
    ): Boolean {
        return safeNativeOperation(
            operation = "Bot Defense: $operation",
            fallback = { 
                Log.w(TAG, "Bot defense operation failed, continuing without protection: $operation")
                false 
            }
        ) {
            block()
            true
        } ?: false
    }

    /**
     * Initializes native library safety measures.
     * Should be called during application startup.
     */
    fun initialize() {
        Log.i(TAG, "Initializing native library protection for Appdome integration")
        
        // Set up uncaught exception handler for native crashes
        val defaultHandler = Thread.getDefaultUncaughtExceptionHandler()
        Thread.setDefaultUncaughtExceptionHandler { thread, exception ->
            if (isNativeLibraryCrash(exception)) {
                Log.e(TAG, "Native library crash detected in thread ${thread.name}", exception)
                // Log the crash but let the default handler deal with it
                // This ensures we get logs even for fatal crashes
            }
            defaultHandler?.uncaughtException(thread, exception)
        }
    }

    /**
     * Checks if an exception is likely caused by a native library crash.
     */
    private fun isNativeLibraryCrash(exception: Throwable): Boolean {
        val message = exception.message ?: ""
        val stackTrace = exception.stackTraceToString()
        
        return message.contains("SIGSEGV", ignoreCase = true) ||
               message.contains("RSA_size", ignoreCase = true) ||
               message.contains("libloader", ignoreCase = true) ||
               stackTrace.contains("libloader.so") ||
               stackTrace.contains("RSA_size") ||
               stackTrace.contains("encryptHeaderWithPublicKey") ||
               stackTrace.contains("mobilebot_defense")
    }
}