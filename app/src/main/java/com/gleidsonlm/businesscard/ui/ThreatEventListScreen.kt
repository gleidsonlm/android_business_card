package com.gleidsonlm.businesscard.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.gleidsonlm.businesscard.R
import com.gleidsonlm.businesscard.model.ThreatEventData
import com.gleidsonlm.businesscard.ui.components.ThreatEventCard
import com.gleidsonlm.businesscard.ui.viewmodel.ThreatEventListViewModel

/**
 * Screen that displays a list of all received threat events.
 * 
 * Shows threat events as cards that can be tapped to view details.
 * Includes options to refresh the list and clear all events.
 * 
 * @param onEventClick Callback when a threat event card is clicked
 * @param onBackClick Callback when the back button is clicked
 * @param viewModel The ViewModel managing the threat events list state
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ThreatEventListScreen(
    onEventClick: (ThreatEventData) -> Unit,
    onBackClick: (() -> Unit)? = null,
    viewModel: ThreatEventListViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    var showClearDialog by remember { mutableStateOf(false) }
    
    // Show error snackbar if there's an error
    LaunchedEffect(uiState.error) {
        if (uiState.error != null) {
            // Error will be cleared automatically after showing
            // In a real app, you might want to show a Snackbar here
        }
    }
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.threat_events_title)) },
                navigationIcon = {
                    onBackClick?.let {
                        IconButton(onClick = it) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = stringResource(R.string.back)
                            )
                        }
                    }
                },
                actions = {
                    // Refresh button
                    IconButton(onClick = { viewModel.refresh() }) {
                        Icon(
                            imageVector = Icons.Default.Refresh,
                            contentDescription = stringResource(R.string.refresh_events)
                        )
                    }
                    
                    // Clear all events button
                    IconButton(onClick = { showClearDialog = true }) {
                        Icon(
                            imageVector = Icons.Default.Delete,
                            contentDescription = stringResource(R.string.clear_all_events)
                        )
                    }
                }
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            when {
                uiState.isLoading -> {
                    // Loading state
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                }
                
                uiState.events.isEmpty() -> {
                    // Empty state
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(32.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = stringResource(R.string.no_threat_events_title),
                            style = MaterialTheme.typography.headlineSmall,
                            textAlign = TextAlign.Center
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = stringResource(R.string.no_threat_events_message),
                            style = MaterialTheme.typography.bodyMedium,
                            textAlign = TextAlign.Center,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
                
                else -> {
                    // Events list
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(16.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(uiState.events) { event ->
                            ThreatEventCard(
                                threatEvent = event,
                                onCardClick = onEventClick
                            )
                        }
                    }
                }
            }
            
            // Error message
            uiState.error?.let { error ->
                Snackbar(
                    modifier = Modifier.align(Alignment.BottomCenter),
                    action = {
                        TextButton(onClick = { viewModel.clearError() }) {
                            Text(stringResource(R.string.dismiss))
                        }
                    }
                ) {
                    Text(error)
                }
            }
        }
    }
    
    // Clear confirmation dialog
    if (showClearDialog) {
        AlertDialog(
            onDismissRequest = { showClearDialog = false },
            title = { Text(stringResource(R.string.clear_events_dialog_title)) },
            text = { Text(stringResource(R.string.clear_events_dialog_message)) },
            confirmButton = {
                TextButton(
                    onClick = {
                        viewModel.clearAllEvents()
                        showClearDialog = false
                    }
                ) {
                    Text(stringResource(R.string.clear_events_confirm))
                }
            },
            dismissButton = {
                TextButton(onClick = { showClearDialog = false }) {
                    Text(stringResource(R.string.clear_events_cancel))
                }
            }
        )
    }
}