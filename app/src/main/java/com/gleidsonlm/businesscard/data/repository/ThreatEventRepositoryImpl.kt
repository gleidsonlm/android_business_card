package com.gleidsonlm.businesscard.data.repository

import android.content.Context
import com.gleidsonlm.businesscard.model.ThreatEventData
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Implementation of [ThreatEventRepository] that uses SharedPreferences for persistence.
 * 
 * This follows the same pattern as the existing UserRepositoryImpl in the codebase,
 * using SharedPreferences with Gson for JSON serialization/deserialization.
 */
@Singleton
class ThreatEventRepositoryImpl @Inject constructor(
    private val context: Context
) : ThreatEventRepository {
    
    private val sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
    private val gson = Gson()
    
    // In-memory cache for reactive updates
    private val _eventsFlow = MutableStateFlow<List<ThreatEventData>>(emptyList())
    
    init {
        // Load existing events on initialization
        loadEventsFromStorage()
    }
    
    override suspend fun saveEvent(event: ThreatEventData) {
        val currentEvents = _eventsFlow.value.toMutableList()
        currentEvents.add(0, event) // Add to beginning for most recent first
        
        // Keep only the last MAX_EVENTS to prevent unlimited growth
        if (currentEvents.size > MAX_EVENTS) {
            currentEvents.removeAt(currentEvents.size - 1)
        }
        
        saveEventsToStorage(currentEvents)
        _eventsFlow.value = currentEvents
    }
    
    override fun getAllEvents(): Flow<List<ThreatEventData>> {
        return _eventsFlow.asStateFlow()
    }
    
    override suspend fun getEventById(eventId: String): ThreatEventData? {
        return _eventsFlow.value.find { it.id == eventId }
    }
    
    override suspend fun clearAllEvents() {
        sharedPreferences.edit().remove(KEY_EVENTS).apply()
        _eventsFlow.value = emptyList()
    }
    
    private fun loadEventsFromStorage() {
        val eventsJson = sharedPreferences.getString(KEY_EVENTS, null)
        if (eventsJson != null) {
            try {
                val type = object : TypeToken<List<ThreatEventData>>() {}.type
                val events: List<ThreatEventData> = gson.fromJson(eventsJson, type)
                _eventsFlow.value = events
            } catch (e: Exception) {
                // If JSON parsing fails, start with empty list
                _eventsFlow.value = emptyList()
            }
        }
    }
    
    private fun saveEventsToStorage(events: List<ThreatEventData>) {
        val eventsJson = gson.toJson(events)
        sharedPreferences.edit().putString(KEY_EVENTS, eventsJson).apply()
    }
    
    companion object {
        private const val PREF_NAME = "threat_events_prefs"
        private const val KEY_EVENTS = "stored_events"
        private const val MAX_EVENTS = 100 // Limit to prevent excessive storage usage
    }
}