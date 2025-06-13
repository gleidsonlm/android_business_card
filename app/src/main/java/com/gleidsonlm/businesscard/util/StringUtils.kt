package com.gleidsonlm.businesscard.util

import android.util.Log
import java.math.BigInteger
import java.security.MessageDigest

fun String.toMd5(): String {
    val md = MessageDigest.getInstance("MD5")
    val hash = BigInteger(1, md.digest(toByteArray())).toString(16)
    Log.d("HASH", "toMd5: $hash")
    return hash
}
