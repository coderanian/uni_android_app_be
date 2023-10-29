package de.patternframeworks.busash.auth.service

import de.patternframeworks.busash.user.persistance.User
import java.util.*

interface JwtTokenService {
    fun generateToken(userId: Long): String
    fun getUserIdFromToken(token: String): Long
    fun extractTokenFromPrefix(token: String): String
    fun validateToken(token: String, userDetails: User): Boolean
    fun getExpirationDateFromToken(token: String): Date
}