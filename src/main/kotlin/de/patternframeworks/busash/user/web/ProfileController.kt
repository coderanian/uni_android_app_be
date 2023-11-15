package de.patternframeworks.busash.user.web

import de.patternframeworks.busash.auth.service.JwtTokenService
import de.patternframeworks.busash.error.MainException
import de.patternframeworks.busash.location.LocationRepository
import de.patternframeworks.busash.offer.OfferRepository
import de.patternframeworks.busash.user.persistance.User
import de.patternframeworks.busash.user.persistance.UserRepository
import de.patternframeworks.busash.user.service.UserService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/my-profile")
class ProfileController(
        @Autowired private val userRepository: UserRepository,
        private val jwtTokenService: JwtTokenService,
        private val locationRepository: LocationRepository,
        private val offerRepository: OfferRepository,
        private val userService: UserService
) {

    @GetMapping("")
    fun getUserInformation(@RequestHeader(name = "Authorization") header: String): ResponseEntity<Any> {
        val token = jwtTokenService.extractTokenFromPrefix(header)
        val userId = jwtTokenService.getUserIdFromToken(token)
        val offerCnt = userService.getOfferCntByUser(userId)
        val user = userRepository.findById(userId)
        val response = mapOf(
            "user" to user,
            "offerCnt" to offerCnt
        )
        return ResponseEntity(response, HttpStatus.OK)
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
            @RequestBody user: User
    ): ResponseEntity<Any> {
        val token = jwtTokenService.extractTokenFromPrefix(header)
        val userId = jwtTokenService.getUserIdFromToken(token)
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
                picture = user.picture,
                password = user.password,
                location = updatedLocation ?: existingUser.location
        )
        userRepository.save(updatedUser)
        return ResponseEntity(updatedUser, HttpStatus.OK)
    }

    /**
     * Simple invocation via token for countByAuthorId function in OfferController.kt
     * @author Konstantin K.
     * @param header token in the authorization
     * @return number of rows in offer table for token bearer
     */
    @GetMapping("/offer-count")
    fun getUserOffersCnt(
        @RequestHeader(name = "Authorization") header: String
    ): Long {
        val token = jwtTokenService.extractTokenFromPrefix(header)
        val userId = jwtTokenService.getUserIdFromToken(token)
        return offerRepository.countByAuthorId(userId)
    }
}