package com.gleidsonlm.businesscard.util

import android.Manifest // Manifest import might still be useful for other permissions, keeping it for now.
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager // PackageManager import might still be useful for other permissions.
import android.net.Uri
import android.os.Build
import android.provider.Settings
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

object PermissionUtils {

    // Made public by removing private modifier
    const val REQUEST_CODE_INSTALL_PACKAGES = 1001
    const val REQUEST_CODE_READ_PHONE_STATE = 1002

    // Track which permissions have been requested to determine if they're permanently denied
    private val requestedPermissions = mutableSetOf<String>()

    /**
     * Checks if the app has the permission to request package installs.
     *
     * @param context The context to use for checking the permission.
     * @return `true` if the permission is granted or not required to be explicitly requested at runtime, `false` otherwise.
     */
    fun hasInstallPackagesPermission(context: Context): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            context.packageManager.canRequestPackageInstalls()
        } else {
            // For versions prior to Android O, declaring the permission in the manifest
            // is generally sufficient. There isn't a runtime request mechanism like for other
            // dangerous permissions. Since we ensure it's in the manifest,
            // we can assume it's available.
            // A more thorough check could involve inspecting PackageInfo.requestedPermissions.
            true
        }
    }

    /**
     * Requests the permission to install packages from the user if needed.
     * On Android O and above, this may direct the user to system settings.
     * On pre-O versions, this method currently does nothing as the permission is granted at install time.
     *
     * @param activity The activity that is requesting the permission.
     */
    fun requestInstallPackagesPermission(activity: Activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            if (!activity.packageManager.canRequestPackageInstalls()) {
                // Intent to navigate to the settings page for the app to allow package installs.
                // This is the standard way to request this permission on Android O+.
                val intent = Intent(Settings.ACTION_MANAGE_UNKNOWN_APP_SOURCES).apply {
                    data = Uri.parse("package:${activity.packageName}")
                }
                activity.startActivityForResult(intent, REQUEST_CODE_INSTALL_PACKAGES)
            }
        }
        // For pre-O versions, no runtime request is typically made for REQUEST_INSTALL_PACKAGES.
        // If the permission is in the manifest, it's considered granted.
    }

    /**
     * Checks if the app has the READ_PHONE_STATE permission.
     * This permission is required for Appdome SIM swap detection capability.
     *
     * @param context The context to use for checking the permission.
     * @return `true` if the permission is granted, `false` otherwise.
     */
    fun hasReadPhoneStatePermission(context: Context): Boolean {
        return ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.READ_PHONE_STATE
        ) == PackageManager.PERMISSION_GRANTED
    }

    /**
     * Requests the READ_PHONE_STATE permission from the user.
     * This permission is required for Appdome SIM swap detection capability to monitor
     * SIM state changes as part of enhanced security against SIM swap attacks.
     *
     * Reference: https://www.appdome.com/how-to/account-takeover-prevention/account-protection/detect-sim-card-swapping-in-android-ios/
     *
     * @param activity The activity that is requesting the permission.
     */
    fun requestReadPhoneStatePermission(activity: Activity) {
        // Mark permission as requested for permanent denial detection
        requestedPermissions.add(Manifest.permission.READ_PHONE_STATE)
        
        ActivityCompat.requestPermissions(
            activity,
            arrayOf(Manifest.permission.READ_PHONE_STATE),
            REQUEST_CODE_READ_PHONE_STATE
        )
    }

    /**
     * Checks if the READ_PHONE_STATE permission has been requested before.
     * This is used to determine if a permission denial is permanent (user selected "Don't ask again").
     *
     * @param activity The activity to check (parameter kept for consistency with other permission methods).
     * @return `true` if the permission has been requested before, `false` otherwise.
     */
    fun hasRequestedReadPhoneStatePermission(activity: Activity): Boolean {
        return requestedPermissions.contains(Manifest.permission.READ_PHONE_STATE)
    }

    /**
     * Checks if the user has denied the permission and selected "Don't ask again".
     *
     * @param activity The activity to check.
     * @return `true` if the permission was permanently denied, `false` otherwise.
     */
    fun isReadPhoneStatePermissionPermanentlyDenied(activity: Activity): Boolean {
        return !ActivityCompat.shouldShowRequestPermissionRationale(
            activity,
            Manifest.permission.READ_PHONE_STATE
        ) && !hasReadPhoneStatePermission(activity)
            && hasRequestedReadPhoneStatePermission(activity)
    }
}
