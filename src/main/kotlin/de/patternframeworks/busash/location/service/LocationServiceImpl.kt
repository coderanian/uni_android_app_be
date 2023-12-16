package de.patternframeworks.busash.location.service

import de.patternframeworks.busash.location.persistance.Location
import de.patternframeworks.busash.location.persistance.LocationRepository
import de.patternframeworks.busash.model.LocationDto
import org.springframework.stereotype.Service

@Service
class LocationServiceImpl(
    private val locationRepository: LocationRepository,
    private val locationMapper: LocationMapper
): LocationService {
    override fun saveLocationOrGetExisting(locationDto: LocationDto): Location {
        var locationId: Long? = null
        locationRepository.findAll().forEach {

            if (it.latitude.equals(locationDto.latitude)) {
                if (it.longitude.equals(locationDto.longitude)) {
                    locationId = it.locationId
                }
            }
        }
        if (locationId == null) {
            return locationRepository.save(locationMapper.dtoToLocation(locationDto))
        }
        return locationRepository.findById(locationId!!).orElse(null)
    }
}