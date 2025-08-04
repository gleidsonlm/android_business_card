package com.gleidsonlm.businesscard.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.gleidsonlm.businesscard.R
import com.gleidsonlm.businesscard.model.ThreatEventData

/**
 * Composable function that defines the UI for displaying threat event details.
 *
 * This screen shows a list of key-value pairs representing the data associated with a
 * received threat event. It handles cases where threat data might be null or if specific
 * fields within the data are empty.
 *
 * Note: UI labels for data fields are currently hardcoded. For production,
 * these should be externalized to string resources (e.g., `R.string.label_device_id`).
 * New string resources like `R.string.threatevent_no_data` should also be added.
 *
 * @param threatEventData The [ThreatEventData] object containing details of the threat.
 *                        If null, a "No data available" message is displayed.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ThreatEventScreenContent(threatEventData: ThreatEventData?) {
    Scaffold(
        topBar = {
            TopAppBar(title = { Text(stringResource(id = R.string.threatevent_screen_title)) })
        }
    ) { paddingValues ->
        if (threatEventData == null) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(16.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = stringResource(id = R.string.threatevent_no_data),
                    style = MaterialTheme.typography.bodyLarge
                )
            }
        } else {
            // Note: Labels are hardcoded as per subtask instructions.
            // These should be replaced with stringResource(id = R.string.your_label_key) in a real app.
            val eventDetails = listOfNotNull(
                ("Event Message" to threatEventData.message),
                ("Default Message" to threatEventData.defaultMessage),
                ("Internal Error" to threatEventData.internalError),
                ("Timestamp" to threatEventData.timeStamp),
                ("Device ID" to threatEventData.deviceID),
                ("Device Model" to threatEventData.deviceModel),
                ("OS Version" to threatEventData.osVersion),
                ("Kernel Info" to threatEventData.kernelInfo),
                ("Device Manufacturer" to threatEventData.deviceManufacturer),
                ("Fused App Token" to threatEventData.fusedAppToken),
                ("Carrier PLMN" to threatEventData.carrierPlmn),
                ("Device Brand" to threatEventData.deviceBrand),
                ("Device Board" to threatEventData.deviceBoard),
                ("Build Host" to threatEventData.buildHost),
                ("Build User" to threatEventData.buildUser),
                ("SDK Version" to threatEventData.sdkVersion),
                ("Failsafe Enforcement" to threatEventData.failSafeEnforce),
                ("External ID" to threatEventData.externalID),
                ("Reason Code" to threatEventData.reasonCode),
                ("Build Date" to threatEventData.buildDate),
                ("Device Platform" to threatEventData.devicePlatform),
                ("Carrier Name" to threatEventData.carrierName),
                ("Updated OS Version" to threatEventData.updatedOSVersion),
                ("Time Zone" to threatEventData.timeZone),
                ("Device Face Down" to threatEventData.deviceFaceDown),
                ("Location Longitude" to threatEventData.locationLong),
                ("Location Latitude" to threatEventData.locationLat),
                ("Location State" to threatEventData.locationState),
                ("WiFi SSID" to threatEventData.wifiSsid),
                ("WiFi SSID Permission" to threatEventData.wifiSsidPermissionStatus),
                ("Threat Code" to threatEventData.threatCode)
            ).filter { !it.second.isNullOrBlank() }

            if (eventDetails.isEmpty()) {
                 Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues)
                        .padding(16.dp),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        // Assumes R.string.threatevent_default_message is "A security event has occurred. No specific message was provided."
                        // This is a fallback if no specific details are available to display.
                        text = stringResource(id = R.string.threatevent_default_message),
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
            } else {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues)
                        .padding(horizontal = 8.dp, vertical = 8.dp),
                    contentPadding = PaddingValues(vertical = 8.dp)
                ) {
                    items(eventDetails) { detail ->
                        Row(modifier = Modifier.padding(vertical = 4.dp)) {
                            Text(
                                text = "${detail.first}: ",
                                style = MaterialTheme.typography.bodyMedium,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.width(150.dp)
                            )
                            Text(
                                text = detail.second ?: "N/A", // Should not be "N/A" due to filter above, but good practice.
                                style = MaterialTheme.typography.bodyMedium,
                                modifier = Modifier.weight(1f)
                            )
                        }
                    }
                }
            }
        }
    }
}

// Reminder for string resources to be added/confirmed in `app/src/main/res/values/strings.xml`:
// <string name="threatevent_screen_title">Threat Event Details</string> (assumed to exist)
// <string name="threatevent_default_message">A security event has occurred. No specific message was provided.</string> (assumed to exist)
// <string name="threatevent_no_data">No threat event data available.</string> (newly mentioned)
// Also, consider externalizing labels used in `eventDetails` list, e.g.:
// <string name="label_event_message">Event Message</string>
// <string name="label_default_message">Default Message</string>
// ... and so on for all other labels.
