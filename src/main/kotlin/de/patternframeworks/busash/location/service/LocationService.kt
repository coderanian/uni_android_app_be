package de.patternframeworks.busash.location.service

import de.patternframeworks.busash.location.persistance.Location
import de.patternframeworks.busash.model.LocationDto

interface LocationService {
    fun saveLocationOrGetExisting(locationDto: LocationDto): Location
}
