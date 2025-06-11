package com.gleidsonlm.businesscard

// Log is already imported
// import com.gleidsonlm.businesscard.R // R is usually available without explicit import in .kt files in module
// painterResource and stringResource are already imported
// androidx.compose.foundation.Image is already imported
// Other necessary imports like MaterialTheme, Surface, padding, Alignment are already present
// androidx.compose.material3.Button is already imported
// androidx.compose.foundation.layout.fillMaxSize is already imported
// UserData is already imported via com.gleidsonlm.businesscard.ui.UserData
// import androidx.compose.ui.platform.LocalContext // Already imported
// import androidx.compose.runtime.LaunchedEffect // Already imported
// import androidx.compose.ui.graphics.ImageBitmap // Already imported
// import androidx.compose.ui.graphics.asImageBitmap // Already imported
// import com.gleidsonlm.businesscard.util.VCardHelper // Already imported
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
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.AccountCircle
import androidx.compose.material.icons.rounded.Call
import androidx.compose.material.icons.rounded.Email
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.net.toUri
import coil.compose.AsyncImage
import com.gleidsonlm.businesscard.ui.UserData
import com.gleidsonlm.businesscard.ui.UserInputScreen
import com.gleidsonlm.businesscard.ui.theme.BusinessCardTheme
import com.gleidsonlm.businesscard.util.DataStore
import com.gleidsonlm.businesscard.util.VCardHelper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val context = LocalContext.current
            var currentData by remember { mutableStateOf<UserData?>(null) }
            var showInputScreen by remember { mutableStateOf(true) }
            // var qrCodeImageBitmap by remember { mutableStateOf<ImageBitmap?>(null) } // DELETED

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
                        UserInputScreen(
                            existingData = currentData, // Pass currentData to prefill
                            onSaveClicked = { userData ->
                                DataStore.saveUserData(context, userData)
                                currentData = userData
                                showInputScreen = false
                                Log.d("MainActivity", "UserData Saved: $userData")
                            }
                        )
                    } else {
                        Box(modifier = Modifier.fillMaxSize()) {
                            BusinessCardApp(
                                userData = currentData
                                // onShowQrCodeClicked lambda removed
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

                    // Dialog for QR Code display // DELETED
                    // if (qrCodeImageBitmap != null) { ... } // DELETED
                }
            }
        }
    }
}

@Composable
private fun BusinessCardApp(userData: UserData?) {
    var qrCodeImageBitmap by remember { mutableStateOf<ImageBitmap?>(null) }

    LaunchedEffect(userData) {
        qrCodeImageBitmap = if (userData != null) {
            val vCardString = withContext(Dispatchers.Default) {
                VCardHelper.generateVCardString(userData)
            }
            val androidBitmap = withContext(Dispatchers.Default) {
                VCardHelper.generateQRCodeBitmap(vCardString)
            }
            androidBitmap?.asImageBitmap()
        } else {
            null
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
        // horizontalAlignment = Alignment.CenterHorizontally // Kept for overall centering
    ) {
        // Top 1/3: QR Code
        Box(
            modifier = Modifier
                .weight(1f) // Can adjust weight if more/less space is needed
                .fillMaxWidth()
                .padding(bottom = 8.dp),
            contentAlignment = Alignment.Center
        ) {
            if (qrCodeImageBitmap != null) {
                Image(
                    bitmap = qrCodeImageBitmap!!,
                    contentDescription = stringResource(R.string.qr_code_content_description),
                    modifier = Modifier.size(160.dp) // Adjust size as needed
                )
            } else if (userData != null) {
                // Show a loading indicator if userData is present but QR code is still generating
                CircularProgressIndicator()
            } else {
                // Placeholder text if no user data (and thus no QR code)
                Text(stringResource(R.string.qr_code_placeholder_text))
            }
        }

        // Middle 1/3: Avatar & Info (BusinessCardFace)
        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
            BusinessCardFace(
                avatarUri = userData?.avatarUri,
                actualFullName = userData?.fullName ?: stringResource(id = R.string.full_name),
                actualTitle = userData?.title ?: stringResource(id = R.string.title)
            )
        }

        // Bottom 1/3: Links & Actions
        Column(
            modifier = Modifier
                .weight(1f) // Can adjust weight
                .fillMaxWidth()
                .padding(top = 8.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center // Vertically center content in this section
        ) {
            BusinessCardLink(
                icon = Icons.Rounded.Call,
                actualLinkText = userData?.phoneNumber?.takeIf { it.isNotBlank() } ?: stringResource(id = R.string.link_text_call),
                actualLinkUrl = "tel:${userData?.phoneNumber?.takeIf { it.isNotBlank() } ?: ""}"
            )
            Spacer(Modifier.height(8.dp))
            BusinessCardLink(
                icon = Icons.Rounded.Email,
                actualLinkText = userData?.emailAddress?.takeIf { it.isNotBlank() } ?: stringResource(id = R.string.link_text_email),
                actualLinkUrl = "mailto:${userData?.emailAddress?.takeIf { it.isNotBlank() } ?: ""}"
            )
            Spacer(Modifier.height(8.dp))
            BusinessCardLink(
                icon = Icons.Rounded.AccountCircle,
                actualLinkText = userData?.website?.takeIf { it.isNotBlank() } ?: stringResource(id = R.string.link_text_in),
                actualLinkUrl = userData?.website?.takeIf { it.isNotBlank() } ?: "#"
            )
            Spacer(Modifier.height(16.dp))

            val context = LocalContext.current // Context for the share Intent
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
                        }
                    } else {
                        Log.w("ShareVCard", "User data is null, cannot share vCard.")
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(stringResource(R.string.share_vcard_button))
            }
        }
    }
}

@Composable
private fun BusinessCardFace(
    avatarUri: String?, // Changed from image: Painter
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
        AsyncImage(
            model = avatarUri ?: R.drawable.avatar, // Use URI or default drawable
            contentDescription = stringResource(R.string.user_avatar_content_description),
            modifier = Modifier
                .size(120.dp)
                .clip(CircleShape),
            contentScale = ContentScale.Crop,
            placeholder = painterResource(id = R.drawable.avatar), // Default avatar as placeholder
            error = painterResource(id = R.drawable.avatar)       // Default avatar for error
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
            website = "https.sample.com",
            avatarUri = null // avatarUri is already null in sampleData
        )
        // Surface is important for theme colors to apply correctly in preview
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            BusinessCardApp(userData = sampleData) // onShowQrCodeClicked argument removed
        }
    }
}