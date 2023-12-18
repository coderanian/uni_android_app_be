package de.patternframeworks.busash.location.service

import de.patternframeworks.busash.location.persistance.Location
import de.patternframeworks.busash.model.LocationDto

interface LocationService {
    fun saveLocationOrGetExisting(locationDto: LocationDto): Location
    fun calculateDistance(lat1: Double, lon1: Double, lat2: Double, lon2: Double): Double
}
