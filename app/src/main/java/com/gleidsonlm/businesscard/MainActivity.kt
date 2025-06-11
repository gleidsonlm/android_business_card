package com.gleidsonlm.businesscard

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.rounded.AccountCircle
import androidx.compose.material.icons.rounded.Call
import androidx.compose.material.icons.rounded.Email
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import android.util.Log
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.platform.LocalContext
import com.gleidsonlm.businesscard.ui.UserInputScreen
import com.gleidsonlm.businesscard.ui.UserData
import com.gleidsonlm.businesscard.util.DataStore
import com.gleidsonlm.businesscard.util.VCardHelper
import android.content.Intent
// Log is already imported
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.foundation.layout.Box
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
// androidx.compose.foundation.Image is already imported
import androidx.compose.ui.window.Dialog
// Other necessary imports like MaterialTheme, Surface, padding, Alignment are already present
// androidx.compose.material3.Button is already imported
// androidx.compose.foundation.layout.fillMaxSize is already imported
// UserData is already imported via com.gleidsonlm.businesscard.ui.UserData
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.net.toUri
import com.gleidsonlm.businesscard.ui.theme.BusinessCardTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val context = LocalContext.current
            var currentData by remember { mutableStateOf<UserData?>(null) }
            var showInputScreen by remember { mutableStateOf(true) }
            var qrCodeImageBitmap by remember { mutableStateOf<ImageBitmap?>(null) }

            LaunchedEffect(Unit) {
                val loadedData = DataStore.loadUserData(context)
                if (loadedData != null) {
                    currentData = loadedData
                    showInputScreen = false
                }
            }

            BusinessCardTheme {
                Surface(modifier = Modifier.fillMaxSize()) { // Ensure Surface fills size for Box alignment
                    if (showInputScreen) {
                        UserInputScreen(onSaveClicked = { userData ->
                            DataStore.saveUserData(context, userData)
                            currentData = userData
                            showInputScreen = false
                            Log.d("MainActivity", "UserData Saved: $userData")
                        })
                    } else {
                        Box(modifier = Modifier.fillMaxSize()) {
                            BusinessCardApp(
                                userData = currentData,
                                onShowQrCodeClicked = {
                                    if (currentData != null) {
                                        // Ensure currentData is not null before using !!
                                        currentData?.let { nnCurrentData ->
                                            val vCardString = VCardHelper.generateVCardString(nnCurrentData)
                                            val qrBitmap = VCardHelper.generateQRCodeBitmap(vCardString)
                                            if (qrBitmap != null) {
                                                qrCodeImageBitmap = qrBitmap.asImageBitmap()
                                            }
                                        }
                                    }
                                }
                            )
                            Button(
                                onClick = { showInputScreen = true },
                                modifier = Modifier
                                    .align(Alignment.TopEnd)
                                    .padding(16.dp)
                            ) {
                                Text("Edit")
                            }
                        }
                    }

                    // Dialog for QR Code display
                    if (qrCodeImageBitmap != null) {
                        Dialog(onDismissRequest = { qrCodeImageBitmap = null }) {
                            Surface( // Wrap content in a Surface for theming and shape
                                shape = MaterialTheme.shapes.medium,
                                color = MaterialTheme.colorScheme.surface, // Use theme surface color
                                contentColor = MaterialTheme.colorScheme.onSurface // Use theme onSurface color
                            ) {
                                Box(
                                    contentAlignment = Alignment.Center,
                                    modifier = Modifier.padding(16.dp) // Add padding around the image
                                ) {
                                    Image(
                                        bitmap = qrCodeImageBitmap!!, // qrCodeImageBitmap is checked not null here
                                        contentDescription = "Contact QR Code"
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun BusinessCardApp(userData: UserData?, onShowQrCodeClicked: () -> Unit) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        BusinessCardFace(
            image = painterResource(R.drawable.avatar),
            actualFullName = userData?.fullName ?: stringResource(R.string.full_name),
            actualTitle = userData?.title ?: stringResource(R.string.title),
        )
    }
    Column (
        modifier = Modifier.fillMaxSize().padding(16.dp),
        verticalArrangement = Arrangement.Bottom,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        BusinessCardLink(
            icon = Icons.Rounded.Call,
            actualLinkText = userData?.phoneNumber ?: stringResource(R.string.link_text_call),
            actualLinkUrl = "tel:${userData?.phoneNumber ?: ""}",
        )
        BusinessCardLink(
            icon = Icons.Rounded.Email,
            actualLinkText = userData?.emailAddress ?: stringResource(R.string.link_text_email),
            actualLinkUrl = "mailto:${userData?.emailAddress ?: ""}",
        )
        BusinessCardLink(
            icon = Icons.Rounded.AccountCircle, // Kept icon, text/URL now from website
            actualLinkText = userData?.website ?: stringResource(R.string.link_text_in),
            actualLinkUrl = userData?.website ?: "#",
        )
        Spacer(modifier = Modifier.height(16.dp)) // Reduced spacer before new button
        Button(
            onClick = {
                if (userData != null) {
                    val vCardString = VCardHelper.generateVCardString(userData)
                    val intent = Intent(Intent.ACTION_SEND).apply {
                        type = "text/x-vcard"
                        putExtra(Intent.EXTRA_TEXT, vCardString)
                    }
                    try {
                        context.startActivity(Intent.createChooser(intent, "Share vCard"))
                    } catch (e: Exception) {
                        Log.e("ShareVCard", "Failed to start activity for sharing vCard", e)
                        // Optionally, show a Toast to the user that sharing failed
                        // Toast.makeText(context, "Could not share vCard", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Log.w("ShareVCard", "User data is null, cannot share vCard.")
                    // Optionally, show a Toast to the user
                    // Toast.makeText(context, "No data to share", Toast.LENGTH_SHORT).show()
                }
            },
            modifier = Modifier.fillMaxWidth() // Optional: make button full width
        ) {
            Text("Share vCard")
        }
        Spacer(modifier = Modifier.height(8.dp)) // Spacer between buttons
        Button(
            onClick = { onShowQrCodeClicked() },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Show QR Code")
        }
        Spacer(modifier = Modifier.height(40.dp)) // Adjusted spacer after new button
    }
}

@Composable
private fun BusinessCardFace(
    image: Painter,
    actualFullName: String,
    actualTitle: String,
    modifier: Modifier = Modifier
    ) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.background)
    ) {
        Image(
            painter = image,
            contentDescription = null,
        )
        Spacer(Modifier.height(32.dp))
        Text(
            text = actualFullName,
            fontWeight = FontWeight.Bold,
            fontSize = 24.sp,
            color = MaterialTheme.colorScheme.primary,
        )
        Text(
            text = actualTitle,
            color = MaterialTheme.colorScheme.secondary
        )
    }
}

@Composable
private fun BusinessCardLink(
    icon: ImageVector,
    actualLinkText: String,
    actualLinkUrl: String,
    modifier: Modifier = Modifier
){
    val context = LocalContext.current
    Row(
        modifier
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ){
        Icon(
            icon,
            contentDescription = null,
        )
        Spacer(modifier.padding(4.dp))
        Button(
            onClick = { openLink(context, actualLinkUrl) },
            modifier = modifier.defaultMinSize(128.dp)
        ) {
            Text(
                text = actualLinkText,
                modifier = modifier.clickable { openLink(context, actualLinkUrl) }
            )
        }
    }
}

@SuppressLint("QueryPermissionsNeeded")
private fun openLink(context: Context, linkUrl: String) {
    try {
        // Ensure URL is not empty or just "#" before trying to parse
        if (linkUrl.isNotBlank() && linkUrl != "#") {
            val intentUrl = Intent(Intent.ACTION_VIEW, linkUrl.toUri())
            // Check if there's an app to handle this intent
            if (intentUrl.resolveActivity(context.packageManager) != null) {
                context.startActivity(intentUrl)
            } else {
                Log.e("LinkButton", "No activity found to handle URL: $linkUrl")
                // Optionally, show a Toast to the user
                // Toast.makeText(context, "Cannot open link: No app found", Toast.LENGTH_SHORT).show()
            }
        } else {
            Log.w("LinkButton", "Attempted to open an empty or placeholder URL: $linkUrl")
            // Optionally, show a Toast if it's an invalid link action
            // Toast.makeText(context, "Invalid link", Toast.LENGTH_SHORT).show()
        }
    } catch (e: Exception) {
        Log.e("LinkButton", "Error opening URL: $linkUrl", e)
    }
}

@Preview(showBackground = true)
@Composable
fun BusinessCardPreview() {
    BusinessCardTheme {
        val sampleData = UserData(
            fullName = "Sample Name",
            title = "Sample Title",
            phoneNumber = "1234567890",
            emailAddress = "sample@example.com",
            company = "Sample Company",
            website = "https.sample.com"
        )
        // Surface is important for theme colors to apply correctly in preview
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            // For preview, pass a dummy lambda for onShowQrCodeClicked
            BusinessCardApp(userData = sampleData, onShowQrCodeClicked = {})
        }
    }
}