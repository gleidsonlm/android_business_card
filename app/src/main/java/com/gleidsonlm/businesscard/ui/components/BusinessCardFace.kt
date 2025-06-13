package com.gleidsonlm.businesscard.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.gleidsonlm.businesscard.R
import androidx.compose.foundation.shape.CircleShape

@Composable
fun BusinessCardFace(userData: com.gleidsonlm.businesscard.ui.UserData?) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .background(MaterialTheme.colorScheme.surfaceVariant, shape = MaterialTheme.shapes.medium)
            .padding(16.dp)
    ) {
        userData?.let {
            Row(verticalAlignment = Alignment.CenterVertically) {
                AsyncImage(
                    model = it.avatarUri ?: R.drawable.avatar,
                    contentDescription = stringResource(R.string.cont_desc_user_avatar),
                    modifier = Modifier
                        .size(100.dp)
                        .clip(CircleShape),
                    placeholder = painterResource(id = R.drawable.avatar),
                    error = painterResource(id = R.drawable.avatar),
                    contentScale = ContentScale.Crop
                )
                Spacer(modifier = Modifier.width(16.dp))
                Column {
                    Text(it.fullName, style = MaterialTheme.typography.headlineSmall)
                    Text(it.title, style = MaterialTheme.typography.bodyLarge)
                    Text(it.company, style = MaterialTheme.typography.bodyMedium)
                }
            }
        } ?: Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxWidth()) {
            Text(stringResource(R.string.no_data_preview), style = MaterialTheme.typography.bodyLarge)
            Text(stringResource(R.string.no_data_fill_form), style = MaterialTheme.typography.bodyMedium, lineHeight = 20.sp)
        }
    }
}
