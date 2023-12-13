package de.patternframeworks.busash.auth.service

import de.patternframeworks.busash.model.AuthInfoDto
import de.patternframeworks.busash.model.LoginDto
import de.patternframeworks.busash.model.RegisterDto

interface AuthenticationService {
    fun authenticateUser(loginDto: LoginDto): AuthInfoDto
    fun registerUser(registerDto: RegisterDto): AuthInfoDto
}