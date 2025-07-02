package com.gleidsonlm.businesscard

import android.app.Activity // Keep for type hints if any, not strictly necessary for this file's final version
import android.content.Intent
// PackageManager is not directly used for REQUEST_INSTALL_PACKAGES checks after PermissionUtils update
// import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable // Added Composable import
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember // Keep for remember in Composable context
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import com.gleidsonlm.businesscard.model.ThreatEventData
import com.gleidsonlm.businesscard.ui.ThreatEventScreenContent
import com.gleidsonlm.businesscard.ui.theme.BusinessCardTheme
import com.gleidsonlm.businesscard.util.PermissionUtils
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ThreatEventActivity : ComponentActivity() {

    companion object {
        const val EXTRA_THREAT_EVENT_DATA = "com.gleidsonlm.businesscard.EXTRA_THREAT_EVENT_DATA"
        // No longer need a local request code constant, will use PermissionUtils.REQUEST_CODE_INSTALL_PACKAGES
    }

    private var showPermissionDeniedDialog by mutableStateOf(false)
    // initialPermissionCheckDone helps manage the UI state, especially for Android O+
    // when the user is sent to system settings and then returns.
    private var initialPermissionCheckDone by mutableStateOf(false)


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val threatEventData: ThreatEventData? = intent.getParcelableExtra(EXTRA_THREAT_EVENT_DATA)

        // Check for permission. If not granted, and it's the first time (or a retry), request it.
        // For Android O+, this will navigate to system settings.
        // The result of this navigation will be handled in onActivityResult.
        if (!PermissionUtils.hasInstallPackagesPermission(this)) {
            // Only request if we haven't initiated the check yet (e.g. returning from settings is handled in onActivityResult)
            // This check helps prevent re-triggering the permission request if onCreate is called again before onActivityResult.
            if (!initialPermissionCheckDone && isAndroidOAndAbove()) {
                 PermissionUtils.requestInstallPackagesPermission(this)
                 // At this point, for O+, the user is being sent to Settings.
                 // initialPermissionCheckDone will be set to true in onActivityResult.
            } else if (!isAndroidOAndAbove()) {
                // For pre-O, if hasInstallPackagesPermission is false (which it shouldn't be with our updated PermissionUtils)
                // something is wrong, or the permission isn't in the manifest.
                // We set initialPermissionCheckDone = true because there's no async flow.
                initialPermissionCheckDone = true;
                // And we might want to show the dialog directly if it's still not considered granted.
                // Based on current PermissionUtils, hasInstallPackagesPermission is true for pre-O if manifest entry exists.
                // So this path (pre-O and permission denied) should ideally not be hit.
                // If it were, showing dialog would be appropriate:
                // showPermissionDeniedDialog = true
            }
        } else {
            initialPermissionCheckDone = true // Permission was already granted
        }


        setContent {
            BusinessCardTheme {
                if (showPermissionDeniedDialog) {
                    PermissionDeniedDialog {
                        showPermissionDeniedDialog = false // Dismiss dialog
                        // Optionally, could try to request permission again or guide user.
                        // For now, just dismissing. The dialog itself has a settings button.
                    }
                } else if (PermissionUtils.hasInstallPackagesPermission(this)) {
                    // If permission is granted, show the main content.
                    ThreatEventScreenContent(threatEventData = threatEventData)
                } else if (isAndroidOAndAbove() && !initialPermissionCheckDone) {
                    // On Android O+, if permission is not granted and we've just sent the user to settings
                    // (so initialPermissionCheckDone is false), show a loading/waiting message.
                    // This state occurs after requestInstallPackagesPermission is called but before onActivityResult.
                    Text(stringResource(id = R.string.checking_permissions_message))
                } else if (isAndroidOAndAbove() && initialPermissionCheckDone && !PermissionUtils.hasInstallPackagesPermission(this)) {
                    // On Android O+, if we've come back from settings (initialPermissionCheckDone is true)
                    // and permission is still not granted, it means the user denied it or navigated back.
                    // In this case, onActivityResult should have set showPermissionDeniedDialog = true.
                    // This is a fallback text, but the dialog should ideally be shown.
                    Text(stringResource(id = R.string.permission_required_message))
                } else if (!isAndroidOAndAbove() && !PermissionUtils.hasInstallPackagesPermission(this)) {
                    // Pre-O, if permission somehow still not granted (e.g. manifest issue)
                    // This should also ideally lead to the dialog via showPermissionDeniedDialog = true set in onCreate.
                    Text(stringResource(id = R.string.permission_required_message))
                } else {
                     // Default content if no other conditions met (e.g. pre-O and permission granted)
                     // This also covers the initial state for pre-O devices where permission is from manifest.
                     ThreatEventScreenContent(threatEventData = threatEventData)
                }
            }
        }
    }

    private fun isAndroidOAndAbove(): Boolean {
        return android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O
    }

    @Composable
    private fun PermissionDeniedDialog(onDismiss: () -> Unit) {
        val context = LocalContext.current
        AlertDialog(
            onDismissRequest = { onDismiss() },
            title = { Text(stringResource(id = R.string.permission_denied_title)) },
            text = { Text(stringResource(id = R.string.permission_denied_message)) },
            confirmButton = {
                TextButton(onClick = {
                    onDismiss()
                    // Intent to take user directly to "Install unknown apps" settings for this app
                    val intent = if (isAndroidOAndAbove()) {
                        Intent(Settings.ACTION_MANAGE_UNKNOWN_APP_SOURCES).apply {
                            data = Uri.parse("package:${context.packageName}")
                            flags = Intent.FLAG_ACTIVITY_NEW_TASK
                        }
                    } else {
                        // For pre-O, this specific setting screen doesn't exist. Take to general app details.
                        Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
                            data = Uri.parse("package:${context.packageName}")
                            flags = Intent.FLAG_ACTIVITY_NEW_TASK
                        }
                    }
                    context.startActivity(intent)
                }) {
                    Text(stringResource(id = R.string.permission_denied_settings_button))
                }
            },
            dismissButton = {
                TextButton(onClick = { onDismiss() }) {
                    Text(stringResource(id = R.string.dialog_cancel_button))
                }
            }
        )
    }

    @Deprecated("This method is called only for activity results, not permission results from runtime dialogs.")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data) // Call super method

        if (requestCode == PermissionUtils.REQUEST_CODE_INSTALL_PACKAGES) {
            initialPermissionCheckDone = true // Mark that we have returned from the settings activity

            // No matter the resultCode, we re-check the actual permission status
            if (PermissionUtils.hasInstallPackagesPermission(this)) {
                showPermissionDeniedDialog = false // Ensure dialog is hidden if permission was granted
                recreate() // Recreate to refresh UI and apply permission
            } else {
                // User returned from settings without granting the permission
                showPermissionDeniedDialog = true
            }
        }
    }
}
// Placeholder string resource comments (to be replaced with actual R.string references later):
// R.string.permission_denied_title -> "Permission Denied"
// R.string.permission_denied_message_install_packages -> "The app needs permission to install packages to display threat event details. Please grant this permission in the app settings."
// R.string.open_settings -> "Open Settings"
// R.string.cancel -> "Cancel"
// Placeholder for text in setContent if permission is being checked:
// "Please grant the required permission in settings and return to the app."
// "Permission is required to use this feature. Please enable it in settings."
// "Permission is required. Please check app settings."
