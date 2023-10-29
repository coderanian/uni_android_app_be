package de.patternframeworks.busash.auth.web

import de.patternframeworks.busash.auth.service.AuthenticationService
import de.patternframeworks.busash.dtos.*
import de.patternframeworks.busash.error.MainException
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import javax.servlet.http.HttpServletResponse
import javax.servlet.http.Cookie

@RestController
@RequestMapping("/api")
class AuthController(
    private val authenticationService: AuthenticationService
) {

    @PostMapping("/register")
    fun register(@RequestBody body: RegisterDto): ResponseEntity<Any> {
        return try {
            ResponseEntity.ok(authenticationService.registerUser(body))
        } catch (e: MainException) {
            ResponseEntity.badRequest().body(e.message)
        }
    }

    @PostMapping("/login")
    fun login(@RequestBody body: LoginDto): ResponseEntity<Any> {
        return try {
            ResponseEntity.ok(authenticationService.authenticateUser(body))
        } catch (e: MainException) {
            ResponseEntity.badRequest().body(e.message)
        }
    }

    @PostMapping("logout")
    fun logout(response: HttpServletResponse): ResponseEntity<Any> {
        val cookie = Cookie("jwt", "")
        cookie.maxAge = 0

        response.addCookie(cookie)

        return ResponseEntity.ok("successful")
    }
}