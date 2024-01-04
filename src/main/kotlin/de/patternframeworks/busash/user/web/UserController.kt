package de.patternframeworks.busash.user.web

import de.patternframeworks.busash.auth.service.JwtTokenService
import de.patternframeworks.busash.model.ProfileDto
import de.patternframeworks.busash.user.service.UserService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/profile")
class UserController(
        private val userService: UserService,
        private val jwtTokenService: JwtTokenService
) {
    @GetMapping("")
    fun getUserInformation(@RequestHeader(name = "Authorization") header: String): ResponseEntity<ProfileDto> {
        val userId = jwtTokenService.getUserIdFromHeader(header)
        return ResponseEntity.ok(userService.getProfileInformation(userId))
    }

    @PutMapping("")
    fun updateUserInformation(
        @RequestHeader(name = "Authorization") header: String,
        @RequestBody user: ProfileDto
    ): ResponseEntity<ProfileDto> {
        val userId = jwtTokenService.getUserIdFromHeader(header)
        return ResponseEntity.ok(userService.updateProfile(userId, user))
    }
}