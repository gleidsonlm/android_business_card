package com.gleidsonlm.businesscard.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.gleidsonlm.businesscard.R
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Surface

@Composable
fun BusinessCardFace(
    userData: com.gleidsonlm.businesscard.ui.UserData?,
    qrCodeImageBitmap: ImageBitmap?
) {
    Surface(modifier = Modifier.fillMaxSize()) { // Ensure the Surface takes full screen
        Row(
            modifier = Modifier
                .fillMaxSize() // Row takes full screen
                .padding(8.dp) // Optional: add some padding around the Row
        ) {
            // Left Column (25%)
            BoxWithConstraints(
                modifier = Modifier
                    .weight(0.25f)
                    .fillMaxHeight()
                    // .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f)) // Temporary background removed
                    .padding(8.dp),
                contentAlignment = Alignment.Center
            ) {
                val imageSize = maxWidth * 0.8f // Avatar takes 80% of column width
                userData?.let {
                    AsyncImage(
                        model = it.avatarUri ?: R.drawable.avatar,
                        contentDescription = stringResource(R.string.cont_desc_user_avatar),
                        modifier = Modifier
                            .size(imageSize)
                            .clip(CircleShape),
                        placeholder = painterResource(id = R.drawable.avatar),
                        error = painterResource(id = R.drawable.avatar),
                        contentScale = ContentScale.Crop
                    )
                } ?: Text("Avatar Area", style = MaterialTheme.typography.bodySmall)
            }

            // Middle Column (50% for QR Code)
            Column(
                modifier = Modifier
                    .weight(0.5f)
                    .fillMaxHeight()
                    // .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)) // Temporary background removed
                    .padding(8.dp), // Padding around the QR code image
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                if (qrCodeImageBitmap != null) {
                    Image(
                        bitmap = qrCodeImageBitmap,
                        contentDescription = stringResource(R.string.qr_code_content_description),
                        modifier = Modifier
                            .fillMaxWidth() // Fill the width of the column (which is 50% of screen)
                            .aspectRatio(1f) // Maintain square aspect ratio for QR code
                            .padding(4.dp), // Inner padding for the QR code image itself
                        contentScale = ContentScale.Fit // Scale QR code to fit
                    )
                } else if (userData != null) { // Show loader only if userdata exists but QR is not ready
                    CircularProgressIndicator()
                } else { // Placeholder if no data at all
                    Text(stringResource(R.string.qr_code_placeholder_text), style = MaterialTheme.typography.headlineMedium)
                }
            }

            // Right Column (25%)
            BoxWithConstraints(
                modifier = Modifier
                    .weight(0.25f)
                    .fillMaxHeight()
                    // .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f)) // Temporary background removed
                    .padding(8.dp),
                contentAlignment = Alignment.Center
            ) {
                val density = LocalDensity.current
                val availableWidthDp = with(density) { constraints.maxWidth.toDp() }

                // Dynamically calculate font sizes based on available width in the column.
                // Factors (e.g., /10f) and coerceAtMost values are chosen to balance readability and fit.
                // These may require further tuning based on extensive device testing.
                val fullNameFontSize = (availableWidthDp.value / 10f).coerceAtMost(20f).sp
                val titleFontSize = (availableWidthDp.value / 12f).coerceAtMost(16f).sp
                val companyFontSize = (availableWidthDp.value / 14f).coerceAtMost(14f).sp

                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center,
                    modifier = Modifier.fillMaxWidth() // Column takes full width of BoxWithConstraints
                ) {
                    userData?.let {
                        Text(
                            it.fullName,
                            style = MaterialTheme.typography.titleMedium.copy(fontSize = fullNameFontSize),
                            maxLines = 2 // Allow wrapping up to 2 lines
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            it.title,
                            style = MaterialTheme.typography.bodySmall.copy(fontSize = titleFontSize),
                            maxLines = 2
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            it.company,
                            style = MaterialTheme.typography.bodySmall.copy(fontSize = companyFontSize),
                            maxLines = 2
                        )
                    } ?: Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxWidth()) {
                        Text(stringResource(R.string.no_data_preview), style = MaterialTheme.typography.bodySmall)
                        Text(stringResource(R.string.no_data_fill_form), style = MaterialTheme.typography.bodySmall, lineHeight = 16.sp)
                    }
                }
            }
        }
    }
}
