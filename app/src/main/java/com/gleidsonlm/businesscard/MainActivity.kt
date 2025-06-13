package com.gleidsonlm.businesscard

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels // Added import
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.AccountCircle
import androidx.compose.material.icons.rounded.Call
import androidx.compose.material.icons.rounded.Email
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState // Added import
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.gleidsonlm.businesscard.ui.UserData
import com.gleidsonlm.businesscard.ui.UserInputScreen
import com.gleidsonlm.businesscard.ui.components.BusinessCardFace
import com.gleidsonlm.businesscard.ui.components.BusinessCardLink
import com.gleidsonlm.businesscard.ui.theme.BusinessCardTheme
import com.gleidsonlm.businesscard.data.repository.UserRepositoryImpl
import com.gleidsonlm.businesscard.ui.viewmodel.BusinessCardViewModel // Added import
import com.gleidsonlm.businesscard.ui.viewmodel.UserInputViewModel // Added import
import com.gleidsonlm.businesscard.util.VCardHelper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

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
            val context = LocalContext.current
            // var currentData by remember { mutableStateOf<UserData?>(null) } // Commented out
            var showInputScreen by remember { mutableStateOf(true) }

            // Observe ViewModel's currentData for initial load check
            val currentDataFromViewModel by businessCardViewModel.currentData.collectAsState()

            LaunchedEffect(Unit) {
                businessCardViewModel.loadInitialData() // Call ViewModel's load
                // Pass existing data from BusinessCardViewModel to UserInputViewModel if available
                // This helps prefill UserInputScreen if data was loaded by BusinessCardViewModel
                currentDataFromViewModel?.let {
                    userInputViewModel.loadExistingData(it)
                }
            }

            LaunchedEffect(currentDataFromViewModel) { // React to data changes from ViewModel
                if (currentDataFromViewModel != null) {
                    showInputScreen = false
                }
                // Else, if it's null and app just started, showInputScreen remains true (initial state)
            }

            BusinessCardTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    if (showInputScreen) {
                        UserInputScreen(
                            viewModel = userInputViewModel, // Pass UserInputViewModel
                            onSaveCompleted = {
                                // Tell BusinessCardViewModel to reload data, which should reflect the saved changes.
                                businessCardViewModel.loadInitialData() // Refresh data via ViewModel
                                showInputScreen = false
                                Log.d("MainActivity", "Save operation completed by UserInputViewModel")
                            }
                        )
                    } else {
                        Box(modifier = Modifier.fillMaxSize()) {
                            BusinessCardApp(
                                businessCardViewModel = businessCardViewModel // Pass ViewModel
                            )
                            Button(
                                onClick = { showInputScreen = true },
                                modifier = Modifier
                                    .align(Alignment.TopEnd)
                                    .padding(16.dp)
                            ) {
                                Text("Edit") // TODO: Externalize string
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun BusinessCardApp(businessCardViewModel: BusinessCardViewModel) { // Accept ViewModel
    val userData by businessCardViewModel.currentData.collectAsState()
    val qrCodeImageBitmap by businessCardViewModel.qrCodeImageBitmap.collectAsState()

    // QR Code generation is now handled by ViewModel, triggered by userData changes.
    // LaunchedEffect(userData) { ... } for QR code generation removed from here.

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .padding(bottom = 8.dp),
            contentAlignment = Alignment.Center
        ) {
            if (qrCodeImageBitmap != null) {
                Image(
                    bitmap = qrCodeImageBitmap!!,
                    contentDescription = stringResource(R.string.qr_code_content_description),
                    modifier = Modifier.size(160.dp)
                )
            } else if (userData != null) {
                CircularProgressIndicator()
            } else {
                Text(stringResource(R.string.qr_code_placeholder_text))
            }
        }

        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
            BusinessCardFace(
                fullName = userData?.fullName ?: stringResource(id = R.string.full_name),
                title = userData?.title ?: stringResource(id = R.string.title),
                email = userData?.emailAddress ?: stringResource(id = R.string.link_text_email),
                phoneNumber = userData?.phoneNumber ?: stringResource(id = R.string.link_text_call),
                photoUri = userData?.avatarUri
            )
        }

        Column(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .padding(top = 8.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
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

            val context = LocalContext.current
            Button(
                onClick = {
                    if (userData != null) {
                        val vCardString = VCardHelper.generateVCardString(userData!!) // userData is not null here
                        val intent = Intent(Intent.ACTION_SEND).apply {
                            type = "text/x-vcard"
                            putExtra(Intent.EXTRA_TEXT, vCardString)
                        }
                        try {
                            context.startActivity(Intent.createChooser(intent, "Share vCard")) // TODO: Externalize string
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
            avatarUri = null
        )
        // Preview will not work correctly with ViewModel by default.
        // For preview, you might need to pass a mock ViewModel or use a different composable.
        // For now, we comment out the direct ViewModel usage in preview or pass null if appropriate.
        // A better approach for previews with ViewModels will be handled later if needed.
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            // BusinessCardApp(businessCardViewModel = ???) // This preview will need adjustment
            // For now, let's make a simplified preview that doesn't rely on the ViewModel
            // Or pass a dummy UserData directly to a modified BusinessCardApp for preview purposes
             Text("Preview not available with ViewModel integration yet.") // Placeholder
        }
    }
}