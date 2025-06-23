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
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import android.content.res.Configuration
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
    val configuration = LocalConfiguration.current

    Surface(modifier = Modifier.fillMaxSize()) {
        if (configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            // Landscape layout: Row with 3 columns
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(8.dp)
            ) {
                UserAvatarComposable(
                    modifier = Modifier
                        .weight(0.25f)
                        .fillMaxHeight(),
                    userData = userData
                )
                QRCodeComposable(
                    modifier = Modifier
                        .weight(0.5f)
                        .fillMaxHeight(),
                    qrCodeImageBitmap = qrCodeImageBitmap,
                    userData = userData // Pass userData for loading state
                )
                UserDetailsComposable(
                    modifier = Modifier
                        .weight(0.25f)
                        .fillMaxHeight(),
                    userData = userData
                )
            }
        } else {
            // Portrait layout: Column with 3 sections
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp), // More padding for portrait might be nice
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceAround // Distribute space
            ) {
                UserAvatarComposable(
                    modifier = Modifier.weight(0.25f), // Assign some weight or fixed size
                    userData = userData,
                    isPortrait = true
                )
                QRCodeComposable(
                    modifier = Modifier.weight(0.5f), // QR code can take more space
                    qrCodeImageBitmap = qrCodeImageBitmap,
                    userData = userData,
                    isPortrait = true
                )
                UserDetailsComposable(
                    modifier = Modifier.weight(0.25f), // Assign some weight
                    userData = userData,
                    isPortrait = true
                )
            }
        }
    }
}

@Composable
private fun UserAvatarComposable(
    modifier: Modifier = Modifier,
    userData: com.gleidsonlm.businesscard.ui.UserData?,
    isPortrait: Boolean = false
) {
    BoxWithConstraints(
        modifier = modifier.padding(8.dp),
        contentAlignment = Alignment.Center
    ) {
        // In portrait, size might be relative to screen width, not just column.
        // For now, keep it relative to the Box's constraints.
        val imageSize = if (isPortrait) (minOf(maxWidth, maxHeight) * 0.5f) else (maxWidth * 0.8f)

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
}

@Composable
private fun QRCodeComposable(
    modifier: Modifier = Modifier,
    qrCodeImageBitmap: ImageBitmap?,
    userData: com.gleidsonlm.businesscard.ui.UserData?,
    isPortrait: Boolean = false
) {
    Column(
        modifier = modifier.padding(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        if (qrCodeImageBitmap != null) {
            Image(
                bitmap = qrCodeImageBitmap,
                contentDescription = stringResource(R.string.qr_code_content_description),
                modifier = Modifier
                    .fillMaxWidth(if (isPortrait) 0.7f else 1f) // QR takes 70% of width in portrait, full column width in landscape
                    .aspectRatio(1f)
                    .padding(4.dp),
                contentScale = ContentScale.Fit
            )
        } else if (userData != null) {
            CircularProgressIndicator()
        } else {
            Text(stringResource(R.string.qr_code_placeholder_text), style = MaterialTheme.typography.headlineMedium)
        }
    }
}

@Composable
private fun UserDetailsComposable(
    modifier: Modifier = Modifier,
    userData: com.gleidsonlm.businesscard.ui.UserData?,
    isPortrait: Boolean = false
) {
    BoxWithConstraints(
        modifier = modifier.padding(8.dp),
        contentAlignment = Alignment.Center
    ) {
        val density = LocalDensity.current
        // For portrait, constraints.maxWidth might be screen width.
        // For landscape, it's column width.
        val referenceWidthDp = with(density) { constraints.maxWidth.toDp() }

        val fullNameFontSize = (referenceWidthDp.value / (if (isPortrait) 15f else 10f)).coerceAtMost(if (isPortrait) 24f else 20f).sp
        val titleFontSize = (referenceWidthDp.value / (if (isPortrait) 18f else 12f)).coerceAtMost(if (isPortrait) 20f else 16f).sp
        val companyFontSize = (referenceWidthDp.value / (if (isPortrait) 20f else 14f)).coerceAtMost(if (isPortrait) 18f else 14f).sp

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxWidth()
        ) {
            userData?.let {
                Text(it.fullName, style = MaterialTheme.typography.titleMedium.copy(fontSize = fullNameFontSize), maxLines = 2)
                Spacer(modifier = Modifier.height(if (isPortrait) 8.dp else 4.dp))
                Text(it.title, style = MaterialTheme.typography.bodySmall.copy(fontSize = titleFontSize), maxLines = 2)
                Spacer(modifier = Modifier.height(if (isPortrait) 8.dp else 4.dp))
                Text(it.company, style = MaterialTheme.typography.bodySmall.copy(fontSize = companyFontSize), maxLines = 2)
            } ?: Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxWidth()) {
                Text(stringResource(R.string.no_data_preview), style = MaterialTheme.typography.bodySmall)
                Text(stringResource(R.string.no_data_fill_form), style = MaterialTheme.typography.bodySmall, lineHeight = 16.sp)
            }
        }
    }
}
