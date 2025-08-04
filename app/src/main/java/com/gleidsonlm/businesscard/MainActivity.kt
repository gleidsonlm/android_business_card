package com.gleidsonlm.businesscard

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Call
import androidx.compose.material.icons.rounded.Email
import androidx.compose.material.icons.rounded.Info
import androidx.compose.material.icons.rounded.Share
import androidx.compose.material.icons.rounded.Edit // Added for the edit icon
import androidx.compose.material.icons.rounded.Warning // Added for the threat events icon
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton // Added for IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.content.FileProvider
import com.gleidsonlm.businesscard.data.repository.UserRepositoryImpl
import com.gleidsonlm.businesscard.ui.UserData
import com.gleidsonlm.businesscard.ui.UserInputScreen
import com.gleidsonlm.businesscard.ui.ThreatEventListScreen
import com.gleidsonlm.businesscard.ui.components.BusinessCardFace
import com.gleidsonlm.businesscard.ui.components.BusinessCardLink
import com.gleidsonlm.businesscard.ui.theme.BusinessCardTheme
import com.gleidsonlm.businesscard.ui.viewmodel.BusinessCardViewModel
import com.gleidsonlm.businesscard.ui.viewmodel.UserInputViewModel
import com.gleidsonlm.businesscard.util.VCardHelper
import com.gleidsonlm.businesscard.util.PermissionUtils
import dagger.hilt.android.AndroidEntryPoint
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val businessCardViewModel: BusinessCardViewModel by viewModels()
    private val userInputViewModel: UserInputViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        
        // Request READ_PHONE_STATE permission for Appdome SIM swap detection
        if (!PermissionUtils.hasReadPhoneStatePermission(this)) {
            PermissionUtils.requestReadPhoneStatePermission(this)
        }
        
        // Request ACCESS_COARSE_LOCATION permission for Appdome geo-compliance features
        if (!PermissionUtils.hasLocationPermission(this)) {
            PermissionUtils.requestLocationPermission(this)
        }
        
        setContent {
            var showInputScreen by remember { mutableStateOf(true) }
            var showThreatEventsScreen by remember { mutableStateOf(false) }

            // Observe ViewModel's currentData for initial load check
            val currentDataFromViewModel by businessCardViewModel.currentData.collectAsState()

            LaunchedEffect(Unit) {
                businessCardViewModel.loadInitialData() // Call ViewModel's load
                currentDataFromViewModel?.let {
                    userInputViewModel.loadExistingData(it)
                }
            }

            LaunchedEffect(currentDataFromViewModel) { // React to data changes from ViewModel
                if (currentDataFromViewModel != null) {
                    showInputScreen = false
                }
            }

            BusinessCardTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    when {
                        showThreatEventsScreen -> {
                            ThreatEventListScreen(
                                onEventClick = { threatEvent ->
                                    // Navigate to threat event detail screen
                                    val intent = Intent(this@MainActivity, ThreatEventActivity::class.java).apply {
                                        putExtra(ThreatEventActivity.EXTRA_THREAT_EVENT_DATA, threatEvent)
                                    }
                                    startActivity(intent)
                                },
                                onBackClick = {
                                    showThreatEventsScreen = false
                                }
                            )
                        }
                        showInputScreen -> {
                            UserInputScreen(
                                viewModel = userInputViewModel,
                                onSaveCompleted = {
                                    businessCardViewModel.loadInitialData()
                                    showInputScreen = false
                                    Log.d("MainActivity", "Save operation completed by UserInputViewModel")
                                }
                            )
                        }
                        else -> {
                        Box(modifier = Modifier.fillMaxSize()) {
                            BusinessCardApp(
                                businessCardViewModel = businessCardViewModel
                            )
                            // Row to hold Threat Events, Edit and Share buttons
                            Row(
                                modifier = Modifier
                                    .align(Alignment.TopEnd)
                                    .padding(16.dp),
                                horizontalArrangement = Arrangement.End
                            ) {
                                IconButton(
                                    onClick = {
                                        showThreatEventsScreen = true
                                    }
                                ) {
                                    Icon(
                                        imageVector = Icons.Rounded.Warning,
                                        contentDescription = stringResource(R.string.threat_events_button_description)
                                    )
                                }
                                Spacer(modifier = Modifier.width(8.dp))
                                IconButton(
                                    onClick = {
                                        val currentCardData = businessCardViewModel.currentData.value
                                        userInputViewModel.loadExistingData(currentCardData)
                                        showInputScreen = true
                                    }
                                ) {
                                    Icon(
                                        imageVector = Icons.Rounded.Edit,
                                        contentDescription = stringResource(R.string.edit)
                                    )
                                }
                                Spacer(modifier = Modifier.width(8.dp)) // Space between Edit and Share
                                IconButton(
                                    onClick = {
                                        val context = this@MainActivity
                                        val currentData = businessCardViewModel.currentData.value
                                        if (currentData != null) {
                                            val vCardString = VCardHelper.generateVCardString(currentData)
                                            try {
                                                val vcfDir = File(context.cacheDir, "vcards")
                                                if (!vcfDir.exists()) {
                                                    vcfDir.mkdirs()
                                                }
                                                val vcfFile = File(vcfDir, "business_card.vcf")
                                                FileOutputStream(vcfFile).use {
                                                    it.write(vCardString.toByteArray())
                                                }

                                                val vcfUri = FileProvider.getUriForFile(
                                                    context,
                                                    "${context.packageName}.provider",
                                                    vcfFile
                                                )

                                                val shareIntent = Intent(Intent.ACTION_SEND).apply {
                                                    type = "text/x-vcard" // Standard MIME type for vCards
                                                    putExtra(Intent.EXTRA_STREAM, vcfUri)
                                                    addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                                                }
                                                context.startActivity(
                                                    Intent.createChooser(
                                                        shareIntent,
                                                        context.getString(R.string.share_vcard)
                                                    )
                                                )
                                            } catch (e: IOException) {
                                                Log.e("MainActivityShare", "Error creating or sharing vCard file", e)
                                                // Optionally show a Toast to the user
                                            } catch (e: Exception) {
                                                Log.e("MainActivityShare", "An unexpected error occurred during sharing", e)
                                                // Optionally show a Toast to the user
                                            }
                                        } else {
                                            Log.w("MainActivityShare", "User data is null, cannot share vCard.")
                                            // Optionally show a Toast to the user
                                        }
                                    }
                                ) {
                                    Icon(
                                        imageVector = Icons.Rounded.Share,
                                        contentDescription = stringResource(R.string.share_vcard)
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
}

@Composable
private fun BusinessCardApp(businessCardViewModel: BusinessCardViewModel) {
    val userData by businessCardViewModel.currentData.collectAsState()
    val qrCodeImageBitmap by businessCardViewModel.qrCodeImageBitmap.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            // .padding(16.dp) // Padding is now handled by BusinessCardFace's Row
    ) {
        BusinessCardFace(userData = userData, qrCodeImageBitmap = qrCodeImageBitmap)

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp, start = 16.dp, end = 16.dp, bottom = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Bottom
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
                icon = Icons.Rounded.Info,
                actualLinkText = userData?.website?.takeIf { it.isNotBlank() }
                    ?: stringResource(id = R.string.link_text_url),
                actualLinkUrl = userData?.website?.takeIf { it.isNotBlank() } ?: "#"
            )
            Spacer(Modifier.height(16.dp))

            // The existing share button functionality (sharing vCard as text) can be removed or kept as an alternative.
            // For this issue, we are focusing on the new share button in the top bar.
            // To avoid confusion, I will comment out the old button.
            /*
            val context = LocalContext.current
            Button(
                onClick = {
                    val currentData = userData
                    if (currentData != null) {
                        val vCardString = VCardHelper.generateVCardString(currentData)
                        val intent = Intent(Intent.ACTION_SEND).apply {
                            type = "text/x-vcard"
                            putExtra(Intent.EXTRA_TEXT, vCardString)
                        }
                        try {
                            context.startActivity(
                                Intent.createChooser(
                                    intent,
                                    context.getString(R.string.share_vcard)
                                )
                            )
                        } catch (e: Exception) {
                            Log.e("ShareVCard", "Failed to start activity for sharing vCard", e)
                        }
                    } else {
                        Log.w("ShareVCard", "User data is null, cannot share vCard.")
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(
                        horizontal = 16.dp,
                        vertical = 8.dp
                    )
                ) {
                    Icon(
                        imageVector = Icons.Rounded.Share,
                        contentDescription = null
                    )
                    Spacer(Modifier.width(8.dp))
                    Text(stringResource(R.string.share_vcard_button))
                }
            }
            */
        }
    }
}

@Preview(showBackground = true)
@Composable
fun BusinessCardPreview() {
    BusinessCardTheme {
        // For preview, directly call BusinessCardFace with sample data
        // This avoids ViewModel complexities in Preview
        val sampleUserData = UserData(
            fullName = "Sample Name",
            title = "Sample Title",
            phoneNumber = "1234567890",
            emailAddress = "sample@example.com",
            company = "Sample Company",
            website = "https.sample.com",
            avatarUri = null // Or some placeholder painterResource
        )
        // For QR code, you might need a placeholder image or generate a fake ImageBitmap for preview
        // For simplicity, passing null for qrCodeImageBitmap in preview.
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            BusinessCardFace(userData = sampleUserData, qrCodeImageBitmap = null)
            // You could also add a preview for the links section if desired
        }
    }
}