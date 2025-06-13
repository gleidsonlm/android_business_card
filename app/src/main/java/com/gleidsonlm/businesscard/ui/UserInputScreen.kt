package com.gleidsonlm.businesscard.ui

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.net.Uri
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.collectAsState // Added import
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import coil.compose.AsyncImage
import com.gleidsonlm.businesscard.R
import com.gleidsonlm.businesscard.ui.viewmodel.UserInputViewModel // Added import
import java.io.File

@Composable
fun UserInputScreen(viewModel: UserInputViewModel, onSaveCompleted: () -> Unit) {
    // Collect states from ViewModel
    val fullName by viewModel.fullName.collectAsState()
    val title by viewModel.title.collectAsState()
    val phoneNumber by viewModel.phoneNumber.collectAsState()
    val emailAddress by viewModel.emailAddress.collectAsState()
    val company by viewModel.company.collectAsState()
    val website by viewModel.website.collectAsState()
    val avatarUri by viewModel.avatarUri.collectAsState()

    // It's important that loadExistingData is called before this composable is shown
    // if there's existing data to show. This is handled in MainActivity.

    val imagePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia()
    ) { uri: Uri? ->
        viewModel.onGalleryImageSelected(uri)
    }

    val context = LocalContext.current

    var tempCameraUri by remember { mutableStateOf<Uri?>(null) }

    fun createImageUri(context: Context): Uri {
        val imageFile = File(context.cacheDir, "images/pic_${System.currentTimeMillis()}.jpg")
        imageFile.parentFile?.mkdirs()
        return FileProvider.getUriForFile(
            context,
            "${context.packageName}.provider",
            imageFile
        )
    }

    val takePictureLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture()
    ) { success: Boolean ->
        if (success) {
            viewModel.onPhotoTaken(tempCameraUri)
        }
        tempCameraUri = null // Reset after use
    }

    val cameraPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            val newUri = createImageUri(context)
            tempCameraUri = newUri
            takePictureLauncher.launch(newUri)
        } else {
            Log.w("Permission", "CAMERA permission denied.")
        }
    }

    Surface {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                contentAlignment = Alignment.Center
            ) {
                AsyncImage(
                    model = avatarUri ?: R.drawable.avatar, // Reads from ViewModel state
                    contentDescription = "Avatar Preview",
                    modifier = Modifier
                        .size(100.dp)
                        .clip(CircleShape),
                    placeholder = painterResource(id = R.drawable.avatar),
                    error = painterResource(id = R.drawable.avatar),
                    contentScale = ContentScale.Crop
                )
            }
            Button(
                onClick = {
                    viewModel.selectImageFromGalleryClicked()
                    imagePickerLauncher.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Choose from Gallery")
            }
            Spacer(modifier = Modifier.height(8.dp)) // Spacer between gallery and photo buttons
            Button(
                onClick = {
                    viewModel.takePhotoClicked() // Call ViewModel
                    when (ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA)) {
                        PackageManager.PERMISSION_GRANTED -> {
                            val newUri = createImageUri(context)
                            tempCameraUri = newUri
                            takePictureLauncher.launch(newUri)
                        }
                        else -> {
                            cameraPermissionLauncher.launch(Manifest.permission.CAMERA)
                        }
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Take Photo")
            }
            Spacer(modifier = Modifier.height(8.dp)) // Spacer between photo and gravatar buttons
            Button(
                onClick = {
                    viewModel.fetchGravatarClicked(emailAddress) // Pass current emailAddress state
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Fetch Gravatar")
            }
            Spacer(modifier = Modifier.height(16.dp))
            OutlinedTextField(
                value = fullName,
                onValueChange = { viewModel.onFieldChange("fullName", it) },
                label = { Text("Full Name") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(
                value = title,
                onValueChange = { viewModel.onFieldChange("title", it) },
                label = { Text("Title") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(
                value = phoneNumber,
                onValueChange = { viewModel.onFieldChange("phoneNumber", it) },
                label = { Text("Phone Number") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(
                value = emailAddress,
                onValueChange = { viewModel.onFieldChange("emailAddress", it) },
                label = { Text("Email Address") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(
                value = company,
                onValueChange = { viewModel.onFieldChange("company", it) },
                label = { Text("Company") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(
                value = website,
                onValueChange = { viewModel.onFieldChange("website", it) },
                label = { Text("Website") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(16.dp))
            Button(
                onClick = {
                viewModel.saveUserData() // Call ViewModel's save method
                onSaveCompleted()      // Notify MainActivity
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Save")
            }
        }
    }
}

//@Preview(showBackground = true)
//@Composable
//fun UserInputScreenPreview() {
//    // UserInputScreen(viewModel = UserInputViewModel(), onSaveCompleted = {}) // ViewModel needs Hilt or manual repo
//    Text("Preview for UserInputScreen needs ViewModel instance.")
//}
