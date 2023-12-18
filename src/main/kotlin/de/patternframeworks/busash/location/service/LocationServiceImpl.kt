package de.patternframeworks.busash.location.service

import de.patternframeworks.busash.location.persistance.Location
import de.patternframeworks.busash.location.persistance.LocationRepository
import de.patternframeworks.busash.model.LocationDto
import org.springframework.stereotype.Service
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.sqrt

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

    /**
     * Calculate the distance between two locations using their latitude and longitude coordinates.
     *
     * @param lat1 The latitude of the first location.
     * @param lon1 The longitude of the first location.
     * @param lat2 The latitude of the second location.
     * @param lon2 The longitude of the second location.
     * @return The distance between the two locations in kilometers.
     *
     * The distance is calculated using the Haversine formula, which assumes the Earth as a perfect sphere.
     * This formula provides an approximation and may not be accurate for long distances or near the poles.
     */
    override fun calculateDistance(lat1: Double, lon1: Double, lat2: Double, lon2: Double): Double {
        val earthRadius = 6371.0 // Radius of the Earth in kilometers

        // Convert latitude and longitude from degrees to radians
        val lat1Rad = Math.toRadians(lat1)
        val lon1Rad = Math.toRadians(lon1)
        val lat2Rad = Math.toRadians(lat2)
        val lon2Rad = Math.toRadians(lon2)

        // Calculate the differences between the latitudes and longitudes
        val dLat = lat2Rad - lat1Rad
        val dLon = lon2Rad - lon1Rad

        // Apply the Haversine formula
        val a = sin(dLat / 2) * sin(dLat / 2) +
                cos(lat1Rad) * cos(lat2Rad) *
                sin(dLon / 2) * sin(dLon / 2)
        val c = 2 * atan2(sqrt(a), sqrt(1 - a))
        val distance = earthRadius * c

        return distance
    }
}