package de.patternframeworks.busash.user.web

import de.patternframeworks.busash.location.Location
import de.patternframeworks.busash.location.LocationRepository
import de.patternframeworks.busash.user.persistance.User
import de.patternframeworks.busash.user.persistance.UserRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/users")
class UserController(
        @Autowired private val userRepository: UserRepository,
        @Autowired private val locationRepository: LocationRepository
) {
    @GetMapping("")
    fun getAllUsers(): List<User> =
            userRepository.findAll().toList()

    @GetMapping("/{id}")
    fun getUserById(@PathVariable("id") userId: Long): ResponseEntity<User> {
        val user = userRepository.findById(userId).orElse(null)
        return if (user != null) ResponseEntity(user, HttpStatus.OK)
        else ResponseEntity(HttpStatus.NOT_FOUND)
    }

    @PostMapping("")
    fun createUser(@RequestBody user: User): ResponseEntity<User> {
        val createdUser = userRepository.save(user)
        return ResponseEntity(createdUser, HttpStatus.CREATED)
    }


    @PutMapping("/{id}/location")
    fun updateUserLocationById(@PathVariable("id") userId: Long, @RequestBody location: Location): ResponseEntity<User> {
        val updatedLocation = locationRepository.save(location)
        val existingUser = userRepository.findById(userId).orElse(null) ?: return ResponseEntity(HttpStatus.NOT_FOUND)
        val updatedUser = existingUser.copy(
                location = updatedLocation
        )
        userRepository.save(updatedUser)
        return ResponseEntity(updatedUser, HttpStatus.OK)
    }

}