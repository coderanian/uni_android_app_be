package de.patternframeworks.busash.user.web

import de.patternframeworks.busash.auth.service.JwtTokenService
import de.patternframeworks.busash.location.LocationRepository
import de.patternframeworks.busash.user.persistance.UserRepository
import io.jsonwebtoken.Jwts
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.CookieValue
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/my-profile")
class ProfileController(
    @Autowired private val userRepository: UserRepository,
    @Autowired private val locationRepository: LocationRepository,
    private val jwtTokenService: JwtTokenService
) {

    @GetMapping("")
    fun getUserInformation(@RequestHeader(name="Authorization") header: String): ResponseEntity<Any> {
        val token = jwtTokenService.extractTokenFromPrefix(header)
        val userId = jwtTokenService.getUserIdFromToken(token)
        return ResponseEntity(userRepository.findById(userId), HttpStatus.OK)
    }

}