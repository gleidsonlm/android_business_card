package com.gleidsonlm.businesscard.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.gleidsonlm.businesscard.model.ThreatEventData
import java.text.SimpleDateFormat
import java.util.*

/**
 * A card component that displays a summary of a threat event.
 * 
 * Shows the event type, timestamp, and a brief message. Clicking the card
 * will trigger the provided onCardClick callback.
 * 
 * @param threatEvent The threat event data to display
 * @param onCardClick Callback invoked when the card is clicked
 * @param modifier Optional modifier for the card
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ThreatEventCard(
    threatEvent: ThreatEventData,
    onCardClick: (ThreatEventData) -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable { onCardClick(threatEvent) },
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // Event type as title
            Text(
                text = threatEvent.eventType.ifEmpty { "Unknown Threat" },
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )
            
            // Timestamp
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    text = "Detected:",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    text = formatTimestamp(threatEvent.receivedTimestamp),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            
            // Event message (prioritize defaultMessage, fallback to message)
            val displayMessage = threatEvent.defaultMessage?.takeIf { it.isNotBlank() }
                ?: threatEvent.message?.takeIf { it.isNotBlank() }
                ?: "Security threat detected - tap for details"
                
            Text(
                text = displayMessage,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

/**
 * Formats a timestamp (in milliseconds) to a human-readable string.
 */
private fun formatTimestamp(timestamp: Long): String {
    val dateFormat = SimpleDateFormat("MMM dd, yyyy HH:mm", Locale.getDefault())
    return dateFormat.format(Date(timestamp))
}