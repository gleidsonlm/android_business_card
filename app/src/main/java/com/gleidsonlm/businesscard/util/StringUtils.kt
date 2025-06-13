package com.gleidsonlm.businesscard.util

import android.util.Log
import java.math.BigInteger
import java.security.MessageDigest
import kotlin.text.Charsets

fun String.toMd5(): String {
    val md = MessageDigest.getInstance("MD5")
    val hash = BigInteger(1, md.digest(toByteArray())).toString(16)
    Log.d("HASH", "toMd5: $hash")
    return hash
}

fun String.toSha256(): String {
    val processedString = this.trim().lowercase()
    return try {
        val digest = MessageDigest.getInstance("SHA-256")
        val hashBytes = digest.digest(processedString.toByteArray(Charsets.UTF_8))
        // Convert byte array to hex string, ensuring leading zeros
        hashBytes.joinToString("") { "%02x".format(it) }
    } catch (e: Exception) {
        Log.e("StringUtils", "Error generating SHA-256 hash", e)
        "" // Return empty string or handle error as appropriate
    }
}
