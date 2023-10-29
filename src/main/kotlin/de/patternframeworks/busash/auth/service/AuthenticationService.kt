package de.patternframeworks.busash.auth.service

import de.patternframeworks.busash.dtos.AuthInfoDto
import de.patternframeworks.busash.dtos.LoginDto
import de.patternframeworks.busash.dtos.RegisterDto

interface AuthenticationService {
    fun authenticateUser(loginDto: LoginDto): AuthInfoDto
    fun registerUser(registerDto: RegisterDto): AuthInfoDto
}