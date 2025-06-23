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
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
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
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.gleidsonlm.businesscard.data.repository.UserRepositoryImpl
import com.gleidsonlm.businesscard.ui.UserData
import com.gleidsonlm.businesscard.ui.UserInputScreen
import com.gleidsonlm.businesscard.ui.components.BusinessCardFace
import com.gleidsonlm.businesscard.ui.components.BusinessCardLink
import com.gleidsonlm.businesscard.ui.theme.BusinessCardTheme
import com.gleidsonlm.businesscard.ui.viewmodel.BusinessCardViewModel
import com.gleidsonlm.businesscard.ui.viewmodel.UserInputViewModel
import com.gleidsonlm.businesscard.util.VCardHelper

// Simple ViewModel Factory for manual injection until Hilt is set up
class AppViewModelFactory(private val userRepositoryImpl: UserRepositoryImpl) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(BusinessCardViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return BusinessCardViewModel(userRepositoryImpl) as T
        }
        if (modelClass.isAssignableFrom(UserInputViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return UserInputViewModel(userRepositoryImpl) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

class MainActivity : ComponentActivity() {

    private val userRepository by lazy { UserRepositoryImpl(applicationContext) }
    private val businessCardViewModel: BusinessCardViewModel by viewModels {
        AppViewModelFactory(userRepository)
    }
    private val userInputViewModel: UserInputViewModel by viewModels {
        AppViewModelFactory(userRepository)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            var showInputScreen by remember { mutableStateOf(true) }

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
                    if (showInputScreen) {
                        UserInputScreen(
                            viewModel = userInputViewModel,
                            onSaveCompleted = {
                                businessCardViewModel.loadInitialData()
                                showInputScreen = false
                                Log.d("MainActivity", "Save operation completed by UserInputViewModel")
                            }
                        )
                    } else {
                        Box(modifier = Modifier.fillMaxSize()) {
                            BusinessCardApp(
                                businessCardViewModel = businessCardViewModel
                            )
                            Button(
                                onClick = {
                                    val currentCardData = businessCardViewModel.currentData.value
                                    userInputViewModel.loadExistingData(currentCardData)
                                    showInputScreen = true
                                },
                                modifier = Modifier
                                    .align(Alignment.TopEnd)
                                    .padding(16.dp)
                            ) {
                                Text(stringResource(R.string.edit))
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
        // The BusinessCardFace now takes the full screen and handles its own internal padding and layout.
        // It also includes the QR code in its middle column.
        BusinessCardFace(userData = userData, qrCodeImageBitmap = qrCodeImageBitmap)

        // Links and Share button can be part of a bottom section if needed,
        // or integrated differently depending on final design.
        // For now, let's assume they are outside the three-column main face, perhaps below it.
        // If they need to be in one of the columns, BusinessCardFace would need further modification.

        // This Column is for elements below the BusinessCardFace (like links and share button)
        // We might need to adjust weights or structure if BusinessCardFace is not meant to take all space by default.
        // However, the issue implies BusinessCardFace is the main screen content.
        // Let's put the links and share button in a separate Column that doesn't use weight from the top part.
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp, start = 16.dp, end = 16.dp, bottom = 16.dp), // Padding for this section
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Bottom // Arrange items at the bottom of this column
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

            val context = LocalContext.current
            Button(
                onClick = {
                    // Capture userData into a local variable for stable smart casting
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