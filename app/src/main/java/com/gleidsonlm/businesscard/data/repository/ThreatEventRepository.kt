package com.gleidsonlm.businesscard.data.repository

import com.gleidsonlm.businesscard.model.ThreatEventData
import kotlinx.coroutines.flow.Flow

/**
 * Repository interface for managing threat event data.
 * 
 * Provides methods to save, retrieve, and observe threat events.
 * This follows the Repository pattern used in the existing codebase.
 */
interface ThreatEventRepository {
    
    /**
     * Saves a new threat event to storage.
     * 
     * @param event The threat event data to save
     */
    suspend fun saveEvent(event: ThreatEventData)
    
    /**
     * Retrieves all stored threat events.
     * 
     * @return Flow of list of threat events, ordered by most recent first
     */
    fun getAllEvents(): Flow<List<ThreatEventData>>
    
    /**
     * Retrieves a specific threat event by its ID.
     * 
     * @param eventId The unique identifier for the event
     * @return The threat event data if found, null otherwise
     */
    suspend fun getEventById(eventId: String): ThreatEventData?
    
    /**
     * Clears all stored threat events.
     */
    suspend fun clearAllEvents()
}