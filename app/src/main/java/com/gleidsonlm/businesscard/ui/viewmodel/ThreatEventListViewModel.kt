package com.gleidsonlm.businesscard.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gleidsonlm.businesscard.data.repository.ThreatEventRepository
import com.gleidsonlm.businesscard.model.ThreatEventData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel for the Threat Event List screen.
 * 
 * Manages the state and business logic for displaying the list of threat events.
 * Follows the MVVM pattern used throughout the application.
 */
@HiltViewModel
class ThreatEventListViewModel @Inject constructor(
    private val threatEventRepository: ThreatEventRepository
) : ViewModel() {
    
    // UI State for the threat events list
    private val _uiState = MutableStateFlow(ThreatEventListUiState())
    val uiState: StateFlow<ThreatEventListUiState> = _uiState.asStateFlow()
    
    init {
        loadThreatEvents()
    }
    
    /**
     * Loads threat events from the repository and updates the UI state.
     */
    private fun loadThreatEvents() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)
            
            try {
                threatEventRepository.getAllEvents().collect { events ->
                    _uiState.value = _uiState.value.copy(
                        events = events,
                        isLoading = false,
                        error = null
                    )
                }
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = "Failed to load threat events: ${e.message}"
                )
            }
        }
    }
    
    /**
     * Refreshes the threat events list.
     */
    fun refresh() {
        loadThreatEvents()
    }
    
    /**
     * Clears all threat events.
     */
    fun clearAllEvents() {
        viewModelScope.launch {
            try {
                threatEventRepository.clearAllEvents()
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    error = "Failed to clear events: ${e.message}"
                )
            }
        }
    }
    
    /**
     * Clears any error state.
     */
    fun clearError() {
        _uiState.value = _uiState.value.copy(error = null)
    }
}

/**
 * UI state for the Threat Event List screen.
 */
data class ThreatEventListUiState(
    val events: List<ThreatEventData> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)