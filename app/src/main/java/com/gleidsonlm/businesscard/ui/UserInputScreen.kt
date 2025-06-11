package com.gleidsonlm.businesscard.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.gleidsonlm.businesscard.ui.UserData
import coil.compose.AsyncImage
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import com.gleidsonlm.businesscard.R
import androidx.compose.foundation.layout.Box
import androidx.compose.ui.Alignment
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import android.net.Uri
import android.Manifest
import android.os.Build
import androidx.compose.ui.platform.LocalContext
import android.content.pm.PackageManager
import androidx.core.content.ContextCompat
import android.util.Log
import androidx.core.content.FileProvider
import java.io.File
import android.content.Context
import java.security.MessageDigest
import java.math.BigInteger

// MD5 Helper function
private fun String.toMd5(): String {
    return try {
        val md = MessageDigest.getInstance("MD5")
        val messageDigest = md.digest(this.trim().lowercase().toByteArray())
        val no = BigInteger(1, messageDigest)
        var hashtext = no.toString(16)
        while (hashtext.length < 32) {
            hashtext = "0$hashtext"
        }
        hashtext
    } catch (e: Exception) {
        Log.e("MD5", "Error generating MD5 hash", e)
        "" // Return empty string or handle error as appropriate
    }
}

@Composable
fun UserInputScreen(existingData: UserData?, onSaveClicked: (UserData) -> Unit) {
    var fullName by remember { mutableStateOf(existingData?.fullName ?: "") }
    var title by remember { mutableStateOf(existingData?.title ?: "") }
    var phoneNumber by remember { mutableStateOf(existingData?.phoneNumber ?: "") }
    var emailAddress by remember { mutableStateOf(existingData?.emailAddress ?: "") }
    var company by remember { mutableStateOf(existingData?.company ?: "") }
    var website by remember { mutableStateOf(existingData?.website ?: "") }
    var avatarUri by remember { mutableStateOf(existingData?.avatarUri) }

    val imagePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let {
            avatarUri = it.toString()
        }
    }

    val context = LocalContext.current
    val requestPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            imagePickerLauncher.launch("image/*") // For gallery permission
        } else {
            Log.w("Permission", "Storage permission denied.")
        }
    }

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
            tempCameraUri?.let { uri ->
                avatarUri = uri.toString()
            }
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
                    model = avatarUri ?: R.drawable.avatar,
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
                    val permissionToRequest = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                        Manifest.permission.READ_MEDIA_IMAGES
                    } else {
                        Manifest.permission.READ_EXTERNAL_STORAGE
                    }

                    when (ContextCompat.checkSelfPermission(context, permissionToRequest)) {
                        PackageManager.PERMISSION_GRANTED -> {
                            imagePickerLauncher.launch("image/*")
                        }
                        else -> {
                            requestPermissionLauncher.launch(permissionToRequest)
                        }
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Choose from Gallery")
            }
            Spacer(modifier = Modifier.height(8.dp)) // Spacer between gallery and photo buttons
            Button(
                onClick = {
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
                    if (emailAddress.isNotBlank()) {
                        val emailHash = emailAddress.toMd5()
                        if (emailHash.isNotBlank()) {
                            val gravatarUrl = "https://www.gravatar.com/avatar/$emailHash?s=200&d=mp"
                            avatarUri = gravatarUrl // Update the avatarUri state
                            Log.i("Gravatar", "Attempting to load Gravatar from: $gravatarUrl")
                        } else {
                            Log.w("Gravatar", "Email hash is blank, cannot fetch Gravatar.")
                            // Optionally, inform the user that hashing failed
                        }
                    } else {
                        Log.w("Gravatar", "Email address is blank, cannot fetch Gravatar.")
                        // Optionally, inform the user to enter an email
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Fetch Gravatar")
            }
            Spacer(modifier = Modifier.height(16.dp))
            OutlinedTextField(
                value = fullName,
                onValueChange = { fullName = it },
                label = { Text("Full Name") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(
                value = title,
                onValueChange = { title = it },
                label = { Text("Title") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(
                value = phoneNumber,
                onValueChange = { phoneNumber = it },
                label = { Text("Phone Number") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(
                value = emailAddress,
                onValueChange = { emailAddress = it },
                label = { Text("Email Address") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(
                value = company,
                onValueChange = { company = it },
                label = { Text("Company") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(
                value = website,
                onValueChange = { website = it },
                label = { Text("Website") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(16.dp))
            Button(
                onClick = {
                    val userData = UserData(
                        fullName = fullName,
                        title = title,
                        phoneNumber = phoneNumber,
                        emailAddress = emailAddress,
                        company = company,
                        website = website,
                        avatarUri = avatarUri
                    )
                    onSaveClicked(userData)
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Save")
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun UserInputScreenPreview() {
    UserInputScreen(existingData = null, onSaveClicked = {})
}
