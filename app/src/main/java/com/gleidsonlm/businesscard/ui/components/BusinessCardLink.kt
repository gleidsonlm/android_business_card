package com.gleidsonlm.businesscard.ui.components

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri

@SuppressLint("QueryPermissionsNeeded")
private fun openLink(context: Context, url: String) {
    try {
        val intent = Intent(Intent.ACTION_VIEW, url.toUri())
        if (intent.resolveActivity(context.packageManager) != null) {
            context.startActivity(intent)
        } else {
            Log.w("LinkLauncher", "No activity found to handle intent for URL: $url")
            // Optionally: Show a toast to the user that no app can handle the link
        }
    } catch (e: Exception) {
        Log.e("LinkLauncher", "Could not open link $url", e)
        // Optionally: Show a toast to the user about the error
    }
}

@Composable
fun BusinessCardLink(
    icon: ImageVector,
    actualLinkText: String,
    actualLinkUrl: String,
    modifier: Modifier = Modifier // Added modifier parameter
) {
    val context = LocalContext.current
    Button( // Changed from Row to Button for better click handling and accessibility
        onClick = {
            if (actualLinkUrl.isNotBlank() && actualLinkUrl != "#") { // Ensure URL is valid
                openLink(context, actualLinkUrl)
            } else {
                Log.w("LinkClick", "Attempted to open an invalid or placeholder link: $actualLinkUrl")
                // Optionally, provide feedback to the user if the link is a placeholder
            }
        },
        modifier = modifier // Apply the modifier here
            .fillMaxWidth() // Make button fill width by default for consistency
            .defaultMinSize(minHeight = 48.dp) // Ensure a minimum touch target size
    ) {
        Row( // Row is now inside the Button for layout purposes
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp) // Padding for content within button
        ) {
            Icon(imageVector = icon, contentDescription = null) // Decorative icon
            Spacer(Modifier.width(8.dp))
            Text(actualLinkText)
        }
    }
}
