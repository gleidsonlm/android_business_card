package com.gleidsonlm.businesscard.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.gleidsonlm.businesscard.R
import com.gleidsonlm.businesscard.model.ThreatEventData

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ThreatEventScreenContent(threatEventData: ThreatEventData?) {
    Scaffold(
        topBar = {
            TopAppBar(title = { Text(stringResource(id = R.string.threatevent_screen_title)) })
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            val messageToDisplay = if (threatEventData?.message.isNullOrBlank()) {
                stringResource(id = R.string.threatevent_default_message)
            } else {
                threatEventData.message
            }
            Text(
                text = messageToDisplay,
                style = MaterialTheme.typography.bodyLarge
            )
        }
    }
}

// It's good practice to also add string resources.
// These would normally go into `app/src/main/res/values/strings.xml`.
// For now, I'll assume these resources will be created or ask to create them later.
// R.string.threatevent_screen_title -> "Threat Event Details"
// R.string.threatevent_default_message -> "A security event has occurred. No specific message was provided."
