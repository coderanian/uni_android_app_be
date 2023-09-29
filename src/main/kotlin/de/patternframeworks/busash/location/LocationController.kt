package de.patternframeworks.busash.location

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/locations")
class LocationController(@Autowired private val locationRepository: LocationRepository) {
    @GetMapping("")
    fun getAllLocations(): List<Location> =
        locationRepository.findAll().toList()

    @PostMapping("")
    fun createLocation(@RequestBody location: Location): ResponseEntity<Location> {
        var loc_id: Long? = null
        locationRepository.findAll().forEach {

            if (it.latitude.equals(location.latitude)) {
                if (it.longitude.equals(location.longitude)) {
                    loc_id = it.location_id
                }
            }
        }
        if (loc_id != null) {
            return getLocationById(loc_id!!)
        }
        val createdLocation = locationRepository.save(location)
        return ResponseEntity(createdLocation, HttpStatus.CREATED)
    }

    @GetMapping("/{id}")
    fun getLocationById(@PathVariable("id") locationId: Long): ResponseEntity<Location> {
        val location = locationRepository.findById(locationId).orElse(null)
        return if (location != null) ResponseEntity(location, HttpStatus.OK)
        else ResponseEntity(HttpStatus.NOT_FOUND)
    }
}