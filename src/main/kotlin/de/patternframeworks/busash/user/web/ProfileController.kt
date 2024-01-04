package de.patternframeworks.busash.user.web

import de.patternframeworks.busash.auth.service.JwtTokenService
import de.patternframeworks.busash.model.ProfileDto
import de.patternframeworks.busash.error.MainException
import de.patternframeworks.busash.location.persistance.LocationRepository
import de.patternframeworks.busash.user.persistance.UserRepository
import de.patternframeworks.busash.user.service.UserMapper
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/my-profile")
class ProfileController(
    private val userRepository: UserRepository,
    private val jwtTokenService: JwtTokenService,
    private val locationRepository: LocationRepository,
    private val userMapper: UserMapper
) {

    @GetMapping("")
    fun getUserInformation(@RequestHeader(name = "Authorization") header: String): ResponseEntity<Any> {
        val userId = jwtTokenService.getUserIdFromHeader(header)
        val profile = userMapper.userToProfileDto(userRepository.findById(userId).orElse(null))
        return ResponseEntity(profile, HttpStatus.OK)
    }

    /**
     * Updates user information for properties
     * Currently FE form provides currently saved values for property inputs which were not filled out
     * Meaning that, e.g. if no new name was provided information will be overwritten with current name
     * Calling updateLocation method doesn't work, seems to be related to order of transactions in DB
     * Therefore forcing location update in updateUserInformation directly
     * @author Konstantin K.
     * @param header token in the authorization
     * @param user user object corresponding with definition in User.kt
     * @return updated user information
     */
    @PutMapping("")
    fun updateUserInformation(
            @RequestHeader(name = "Authorization") header: String,
            @RequestBody user: ProfileDto
    ): ResponseEntity<Any> {
        val userId = jwtTokenService.getUserIdFromHeader(header)
        val existingUser = userRepository.findById(userId).orElse(null) ?: return ResponseEntity(HttpStatus.NOT_FOUND)
        //Prevent email from being not unique
        if (user.email != existingUser.email) {
            val emailExists = userRepository.findByEmail(user.email)
            if (emailExists.isPresent) {
                throw MainException("E-Mail already exists")
            }
        }
        //Save new location only if provided by user, otherwise null
        val updatedLocation = user.location?.let {
            locationRepository.save(it)
        }
        val updatedUser = existingUser.copy(
                name = user.name,
                email = user.email,
                picture = user.picture ?: existingUser.picture, //WHY IS IT RESET IF NOT PROVIDED IN PUT?
                password = user.newPassword ?: existingUser.password,
                location = updatedLocation ?: existingUser.location

        )
        userRepository.save(updatedUser)
        return ResponseEntity(updatedUser, HttpStatus.OK)
    }
}